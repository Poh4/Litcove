package com.litcove.litcove.data.api

import com.litcove.litcove.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/")
    fun getUsers(): Call<UserResponse>
}