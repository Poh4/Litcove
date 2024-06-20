package com.litcove.litcove.ui.main.explore

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

    fun findBooks(query: String, startIndex: Int, maxResults: Int, onExist: (List<Book>) -> Unit, onNotExist: (List<Book>?) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            val response = bookRepository.findBooks(query, startIndex, maxResults)
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
}