package com.litcove.litcove.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

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
}