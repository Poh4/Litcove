package com.litcove.litcove.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litcove.litcove.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean?>()
    val isDarkMode: LiveData<Boolean?> = _isDarkMode

    fun saveInitialThemeSetting(initialTheme: Boolean) {
        viewModelScope.launch {
            val currentTheme = preferenceRepository.themeSetting.first()
            if (currentTheme == null) {
                preferenceRepository.saveThemeSetting(initialTheme)
            }
        }
    }

    fun loadThemeSetting() {
        viewModelScope.launch {
            preferenceRepository.themeSetting.collect { isDarkMode ->
                _isDarkMode.value = isDarkMode
            }
        }
    }
}

