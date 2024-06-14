package com.litcove.litcove.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class RegisterViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
}