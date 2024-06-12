package com.litcove.litcove.data.repository

import com.litcove.litcove.data.api.ApiConfig
import com.litcove.litcove.data.response.UserResponse
import retrofit2.Call

class UserRepository {
    private val apiService = ApiConfig.getApiService()

    fun getUsers(): Call<UserResponse> {
        return apiService.getUsers()
    }
}