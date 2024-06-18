package com.litcove.litcove.ui.main.mybook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.data.model.Book
import kotlinx.coroutines.launch

class CollectionViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    init {
        init()
    }

    fun init() {
        fetchBooksFromCollection(
            onExist = { books -> _books.value = books },
            onNotExist = { _books.value = emptyList()},
            onFailure = { e ->
                Log.e("CollectionViewModel", "Error fetching books from collection", e)
            }
        )
    }

    private fun fetchBooksFromCollection(onExist: (List<Book>) -> Unit, onNotExist: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            val books = mutableListOf<Book>()
            auth.currentUser?.let {
                db.collection("users")
                    .document(it.uid)
                    .collection("collections")
                    .orderBy("addedAt", Query.Direction.DESCENDING) // Urutkan buku berdasarkan waktu penambahan
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val book = document.toObject(Book::class.java)
                            books.add(book)
                        }
                        if (books.isNotEmpty()) {
                            onExist(books)
                        } else {
                            onNotExist()
                        }
                    }
                    .addOnFailureListener { e -> onFailure(e) }
            }
        }
    }
}