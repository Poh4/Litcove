package com.litcove.litcove.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.litcove.litcove.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean?>()
    val isDarkMode: LiveData<Boolean?> = _isDarkMode

    private val _imageAvatar = MutableLiveData<String>()
    val imageAvatar: LiveData<String> = _imageAvatar

    private val _textName = MutableLiveData<String>()
    val textName: LiveData<String> = _textName

    private val _textJoinedSince = MutableLiveData<String>()
    val textJoinedSince: LiveData<String> = _textJoinedSince

    init {
        fetchUser()
    }

    private fun formatDate(date: Date?): String? {
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return date?.let { outputFormat.format(it) }
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            preferenceRepository.saveThemeSetting(isDarkMode)
        }
    }

    fun loadThemeSetting() {
        viewModelScope.launch {
            preferenceRepository.themeSetting.collect { isDarkMode ->
                _isDarkMode.value = isDarkMode
            }
        }
    }

    fun clearThemeSetting() {
        viewModelScope.launch {
            preferenceRepository.clearThemeSetting()
        }
    }

    fun fetchUser() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val avatar = document.getString("avatar")
                        val name = document.getString("name")
                        val createdAt = document.getTimestamp("createdAt")?.toDate()

                        _imageAvatar.value = avatar ?: ""
                        _textName.value = name ?: "No Name"
                        _textJoinedSince.value = formatDate(createdAt).toString()
                    } else {
                        Log.d("ProfileViewModel", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ProfileViewModel", "Error getting documents.", exception)
                }
        }
    }
}