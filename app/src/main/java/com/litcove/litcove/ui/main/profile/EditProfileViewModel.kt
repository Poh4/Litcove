package com.litcove.litcove.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EditProfileViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()

    private val _imageAvatar = MutableLiveData<String>()
    val imageAvatar: LiveData<String> = _imageAvatar

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> = _fullName

    init {
        fetchUser()
    }

    private fun fetchUser() {
        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val avatar = document.getString("avatar")
                        val name = document.getString("name")

                        _imageAvatar.value = avatar ?: "" // Jika avatar null, gunakan string kosong
                        _fullName.value = name ?: "No Name" // Jika name null, gunakan "No Name"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("EditProfileViewModel", "Error getting documents: ", exception)
                }
        }
    }

    suspend fun saveProfileData(uri: Uri?, fullName: String) {
        var url: String? = null

        if (uri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val profileImagesRef = storageRef.child("profile_images/${UUID.randomUUID()}")
            val uploadTask = profileImagesRef.putFile(uri)

            url = try {
                uploadTask.await()
                profileImagesRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                Log.e("EditProfileActivity", "Upload failed", e)
                null
            }
        }

        if (userId != null) {
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            val oldAvatarUrl = _imageAvatar.value
            val data = hashMapOf(
                "name" to fullName,
                "avatar" to (url ?: "")
            )
            userRef.update(data as Map<String, Any>).addOnSuccessListener {
                Log.d("EditProfileActivity", "DocumentSnapshot successfully written!")
                if (!oldAvatarUrl.isNullOrEmpty()) {
                    val oldAvatarRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldAvatarUrl)
                    oldAvatarRef.delete().addOnSuccessListener {
                        Log.d("EditProfileActivity", "Old profile image successfully deleted!")
                    }.addOnFailureListener { e ->
                        Log.w("EditProfileActivity", "Error deleting old profile image", e)
                    }
                }
            }
                .addOnFailureListener { e ->
                    Log.w("EditProfileActivity", "Error writing document", e)
                }
        }
    }
}
