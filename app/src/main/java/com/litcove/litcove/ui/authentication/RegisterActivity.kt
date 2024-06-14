package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegisterGoogle.setOnClickListener {
            registerWithGoogle()
        }

        val loginPrompt = binding.textViewLoginPrompt
        loginPrompt.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun registerWithGoogle() {
        lifecycleScope.launch {
            GoogleAuthUtils.registerWithGoogle(this@RegisterActivity, auth, lifecycleScope) { user ->
                viewModel.registerUserToFirestore(user)
                if (user != null) {
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}