package com.litcove.litcove.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.litcove.litcove.data.repository.UserRepository
import com.litcove.litcove.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _imageProfile = MutableLiveData<String>()
    val imageProfile: LiveData<String> = _imageProfile

    private val _textName = MutableLiveData<String>()
    val textName: LiveData<String> = _textName

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        fetchUser()
    }

    private fun fetchUser() {
        userRepository.getUsers().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.results[0]
                    _imageProfile.value = user.picture.large
                    _textName.value = "${user.name.first} ${user.name.last}"
                } else {
                    _errorMessage.value = "Failed to load user data: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _errorMessage.value = "Failed to load user data: ${t.message}"
            }
        })
    }
}