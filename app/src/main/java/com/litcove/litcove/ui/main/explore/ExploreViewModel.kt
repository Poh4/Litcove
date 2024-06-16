package com.litcove.litcove.ui.main.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ExploreViewModel : ViewModel() {

    private val _interests = MutableLiveData<List<String>>()
    val interests: LiveData<List<String>> get() = _interests

    init {
        fetchInterests()
    }

    private fun fetchInterests() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        userId?.let { interest ->
            db.collection("users").document(interest).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
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
}