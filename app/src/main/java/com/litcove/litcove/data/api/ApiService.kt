package com.litcove.litcove.data.api

import com.litcove.litcove.data.response.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("volumes")
    suspend fun getBooksBySubject(
        @Query("q") subject: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int
    ): Response<BookResponse>
}