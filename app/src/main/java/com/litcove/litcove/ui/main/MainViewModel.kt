package com.litcove.litcove.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.litcove.litcove.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    fun checkUserAuthentication(onAuthenticated: () -> Unit, onNotAuthenticated: () -> Unit) {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            onAuthenticated()
        } else {
            onNotAuthenticated()
        }
    }

    fun observeThemeSetting(onDark: () -> Unit, onLight: () -> Unit) {
        viewModelScope.launch {
            preferenceRepository.themeSetting.collect { isDarkMode ->
                if (isDarkMode == true) {
                    onDark()
                } else {
                    onLight()
                }
            }
        }
    }
}