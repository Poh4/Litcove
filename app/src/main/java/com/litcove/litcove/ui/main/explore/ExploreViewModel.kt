package com.litcove.litcove.ui.main.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.data.repository.BookRepository
import com.litcove.litcove.utils.BookMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("SameParameterValue")
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _books = MutableLiveData<List<Book>?>()
    val books: LiveData<List<Book>?> get() = _books

    private val _interests = MutableLiveData<List<String>>()
    val interests: LiveData<List<String>> get() = _interests

    init {
        fetchBooks("fiction", 0, 10, { books ->
            _books.value = books
        }, { books ->
            _books.value = books
        }, { throwable ->
            Log.e("ExploreViewModel", "Failed to fetch books: $throwable")
        })
        fetchInterests()
    }

    private fun fetchBooks(recommendation: String, startIndex: Int, maxResults: Int, onExist: (List<Book>) -> Unit, onNotExist: (List<Book>?) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            val response = bookRepository.findBooks(recommendation, startIndex, maxResults)
            if (response.isSuccessful) {
                val books = response.body()?.items?.mapNotNull { it?.let { item -> BookMapper.mapToBook(item) } }
                if (books.isNullOrEmpty()) {
                    onNotExist(books)
                } else {
                    onExist(books)
                }
            } else {
                onFailure(Throwable(response.message()))
            }
        }
    }

    private fun fetchInterests() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        userId?.let { interest ->
            db.collection("users").document(interest).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val interests = document.data?.get("interests") as List<*>
                        _interests.value = interests.map { it.toString() }
                    } else {
                        Log.d("ExploreViewModel", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ExploreViewModel", "Error getting documents.", exception)
                }
        }
    }
}