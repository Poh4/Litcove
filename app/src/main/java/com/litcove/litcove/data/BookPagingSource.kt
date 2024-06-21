package com.litcove.litcove.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.data.repository.BookRepository
import com.litcove.litcove.utils.BookMapper

class BookPagingSource(
    private val repository: BookRepository,
    private val subject: String
) : PagingSource<Int, Book>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val page = params.key ?: 1
        val startIndex = (page - 1) * params.loadSize
        return try {
            val response = repository.getBooksBySubject(subject, startIndex, params.loadSize)
            val books = response.body()?.items?.mapNotNull { it?.let { BookMapper.mapToBook(it) } } ?: emptyList()
            LoadResult.Page(
                data = books,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (books.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
    }
}