package com.litcove.litcove.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val pageCount: Int,
    val categories: List<String>,
    val maturityRating: String,
    val imageLinks: ImageLinks,
    val language: String
) : Parcelable

@Parcelize
data class ImageLinks(
    val thumbnail: String,
    val smallThumbnail: String
) : Parcelable