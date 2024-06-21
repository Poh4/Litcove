package com.litcove.litcove.data.repository

import com.litcove.litcove.data.api.ApiService
import com.litcove.litcove.data.response.BookResponse
import retrofit2.Response
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: ApiService
) {

    //get book recommendation
    suspend fun getBookRecommendation(recommendation: String, startIndex: Int, maxResults: Int): Response<BookResponse> {
        val validMaxResults = if (maxResults > 40) 40 else maxResults
        return apiService.getBookRecommendation(recommendation, startIndex, validMaxResults)
    }

    suspend fun getBooksBySubject(subject: String, startIndex: Int, maxResults: Int): Response<BookResponse> {
        val validMaxResults = if (maxResults > 40) 40 else maxResults
        return apiService.getBooksBySubject(subject, startIndex, validMaxResults)
    }

    suspend fun findBooks(query: String, startIndex: Int, maxResults: Int): Response<BookResponse> {
        val validMaxResults = if (maxResults > 40) 40 else maxResults
        return apiService.findBooks(query, startIndex, validMaxResults)
    }
}