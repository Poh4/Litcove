package com.litcove.litcove.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ChooseInterestsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _interests = MutableLiveData<List<String>>()
    val interests: LiveData<List<String>> get() = _interests

    fun fetchInterests() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        userId?.let { interest ->
            db.collection("users").document(interest).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("interests")) {
                        val interests = document.data?.get("interests") as List<*>
                        _interests.value = interests.map { it.toString() }
                    } else {
                        Log.d("ExploreViewModel", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ExploreViewModel", "Error getting documents.", exception)
                }
        }
    }

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