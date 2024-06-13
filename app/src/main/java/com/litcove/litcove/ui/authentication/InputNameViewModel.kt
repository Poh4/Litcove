package com.litcove.litcove.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class InputNameViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun saveNameToFirestore(userId: String, name: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = hashMapOf(
            "name" to name
        )

        viewModelScope.launch {
            db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun checkIfNameExists(userId: String, onExists: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("name")) {
                        onExists()
                    }
                }
                .addOnFailureListener { exception -> onFailure(exception) }
        }
    }
}