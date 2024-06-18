package com.litcove.litcove.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val bookId: String = "",
    val title: String = "",
    val authors: List<String> = emptyList(),
    val publisher: String = "",
    val publishedDate: String = "",
    val description: String = "",
    val pageCount: Int = 0,
    val categories: List<String> = emptyList(),
    val maturityRating: String = "",
    val thumbnail: String = "",
    val language: String = "",
    val addedAt: Long = 0L
) : Parcelable