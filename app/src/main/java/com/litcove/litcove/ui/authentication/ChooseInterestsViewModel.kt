package com.litcove.litcove.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChooseInterestsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    val hasInterests = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()
    val updateSuccess = MutableLiveData<Boolean>()

    fun updateInterests(userId: String, interests: List<String>) {
        viewModelScope.launch {
            try {
                db.collection("users")
                    .document(userId)
                    .update("interests", interests)
                    .await()
                updateSuccess.postValue(true)
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }

    fun checkInterests(userId: String) {
        viewModelScope.launch {
            try {
                val document = db.collection("users").document(userId).get().await()
                hasInterests.postValue(document.get("interests") != null)
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }
}