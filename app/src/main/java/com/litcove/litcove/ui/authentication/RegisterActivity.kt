package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        viewModel = RegisterViewModel()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegisterGoogle.setOnClickListener {
            GoogleAuthUtils.registerWithGoogle(this, auth, lifecycleScope) { user ->
                registerUserToFirestore(user)
                updateUI(user)
            }
        }

        val loginPrompt = binding.textViewLoginPrompt
        loginPrompt.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun registerUserToFirestore(user: FirebaseUser?) {
        val db = FirebaseFirestore.getInstance()

        val userMap = HashMap<String, Any>()
        userMap["email"] = user?.email ?: ""
        userMap["createdAt"] = Calendar.getInstance().time

        if (user != null) {
            db.collection("users").document(user.uid)
                .set(userMap)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "User profile created for ID: ${user.uid}")
                }
                .addOnFailureListener { e ->
                    Log.w("RegisterActivity", "Error adding document", e)
                }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}