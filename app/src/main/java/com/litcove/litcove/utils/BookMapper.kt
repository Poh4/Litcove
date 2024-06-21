package com.litcove.litcove.utils

import com.litcove.litcove.data.model.Book
import com.litcove.litcove.data.response.ItemsItem

object BookMapper {
    fun mapToBook(item: ItemsItem): Book {
        return Book(
            bookId = item.id ?: "",
            title = item.volumeInfo?.title ?: "",
            authors = item.volumeInfo?.authors?.mapNotNull { it } ?: emptyList(),
            publisher = item.volumeInfo?.publisher ?: "",
            publishedDate = item.volumeInfo?.publishedDate ?: "",
            description = item.volumeInfo?.description ?: "",
            pageCount = item.volumeInfo?.pageCount ?: 0,
            categories = item.volumeInfo?.categories?.mapNotNull { it } ?: emptyList(),
            maturityRating = item.volumeInfo?.maturityRating ?: "",
            thumbnail = item.volumeInfo?.imageLinks?.thumbnail ?: "",
            language = item.volumeInfo?.language ?: ""
        )
    }
}