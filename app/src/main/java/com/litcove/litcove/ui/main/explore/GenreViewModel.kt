package com.litcove.litcove.ui.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.litcove.litcove.data.BookPagingSource
import com.litcove.litcove.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    private val currentGenre = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val booksFlow = currentGenre.flatMapLatest { genre ->
        genre?.let {
            Pager(PagingConfig(pageSize = 20)) {
                BookPagingSource(repository, genre)
            }.flow.cachedIn(viewModelScope)
        } ?: emptyFlow()
    }

    fun setSubject(genre: String) {
        currentGenre.value = genre
    }

    fun refresh() {
        currentGenre.value = currentGenre.value
    }
}