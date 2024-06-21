package com.litcove.litcove.ui.main.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordViewModel : ViewModel() {
    private val user = FirebaseAuth.getInstance().currentUser
    private val email = user?.email

    fun changePassword(oldPassword: String, newPassword: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (email != null) {
            val credential = EmailAuthProvider.getCredential(email, oldPassword)

            user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { tasks ->
                        if (tasks.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure()
                        }
                    }
                } else {
                    onFailure()
                }
            }
        }
    }
}