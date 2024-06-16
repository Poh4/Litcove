package com.litcove.litcove.utils

import com.litcove.litcove.data.model.Book
import com.litcove.litcove.data.model.ImageLinks
import com.litcove.litcove.data.response.ItemsItem

object BookMapper {
    fun mapToBook(item: ItemsItem): Book {
        return Book(
            id = item.id ?: "",
            title = item.volumeInfo?.title ?: "",
            authors = item.volumeInfo?.authors?.mapNotNull { it } ?: emptyList(),
            publisher = item.volumeInfo?.publisher ?: "",
            publishedDate = item.volumeInfo?.publishedDate ?: "",
            description = item.volumeInfo?.description ?: "",
            pageCount = item.volumeInfo?.pageCount ?: 0,
            categories = item.volumeInfo?.categories?.mapNotNull { it } ?: emptyList(),
            maturityRating = item.volumeInfo?.maturityRating ?: "",
            imageLinks = item.volumeInfo?.imageLinks?.let {
                ImageLinks(it.thumbnail ?: "", it.smallThumbnail ?: "")
            } ?: ImageLinks("", ""),
            language = item.volumeInfo?.language ?: ""
        )
    }
}