package com.litcove.litcove.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    private val _isDarkMode = MutableLiveData<Boolean?>()
    val isDarkMode: LiveData<Boolean?> = _isDarkMode

    fun loginWithEmail(email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (Exception?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "Successfully logged in with email and password")
                    val user = auth.currentUser
                    onSuccess(user)
                } else {
                    Log.w("LoginViewModel", "Error logging in with email and password", task.exception)
                    onFailure(task.exception)
                }
            }
    }

    fun registerUserToFirestore(user: FirebaseUser?) {
        if (user != null) {
            val userDocRef = db.collection("users").document(user.uid)
            userDocRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val userMap = hashMapOf<String, Any>()
                    userMap["lastLogin"] = Calendar.getInstance().time

                    userDocRef.update(userMap)
                        .addOnSuccessListener {
                            Log.d("RegisterViewModel", "User profile updated for ID: ${user.uid}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("RegisterViewModel", "Error updating document", e)
                        }
                } else {
                    val userMap = hashMapOf<String, Any>()
                    userMap["email"] = user.email ?: ""
                    userMap["createdAt"] = Calendar.getInstance().time

                    userDocRef.set(userMap)
                        .addOnSuccessListener {
                            Log.d("RegisterViewModel", "User profile created for ID: ${user.uid}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("RegisterViewModel", "Error adding document", e)
                        }
                }
            }
        }
    }

    fun checkIfInterestsExists(userId: String, onExists: () -> Unit, onNotExists: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("interests")) {
                        Log.d("LoginViewModel", "Interests exist")
                        onExists()
                    } else {
                        Log.d("LoginViewModel", "No interests exist")
                        onNotExists()
                    }
                }
                .addOnFailureListener { exception -> onFailure(exception) }
        }
    }

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