package com.litcove.litcove.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ChooseInterestsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun updateInterests(userId: String, interests: List<String>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            db.collection("users")
                .document(userId)
                .update("interests", interests)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }
}