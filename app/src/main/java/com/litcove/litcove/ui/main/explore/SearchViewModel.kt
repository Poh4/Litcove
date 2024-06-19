package com.litcove.litcove.ui.main.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.data.repository.BookRepository
import com.litcove.litcove.utils.BookMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Book>>()
    val searchResults: LiveData<List<Book>> get() = _searchResults

    fun findBooks(query: String, startIndex: Int, maxResults: Int) {
        viewModelScope.launch {
            val response = bookRepository.findBooks(query, startIndex, maxResults)
            if (response.isSuccessful) {
                _searchResults.value = response.body()?.items?.mapNotNull { it?.let { item -> BookMapper.mapToBook(item) } }
            } else {
                Log.e("ExploreViewModel", "Failed to find books: ${response.errorBody()}")
            }
        }
    }
}