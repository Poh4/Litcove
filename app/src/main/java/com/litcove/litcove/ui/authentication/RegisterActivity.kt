package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityRegisterBinding
import com.litcove.litcove.utils.LoadingDialog
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loadingDialog = LoadingDialog(this)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.inputConfirmPassword.text.toString()

            if (validateInput(email, password, confirmPassword)) {
                loadingDialog.startLoading()
                viewModel.registerWithEmail(email, password)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.buttonRegisterGoogle.setOnClickListener {
            registerWithGoogle()
        }

        val loginPrompt = binding.textViewLoginPrompt
        loginPrompt.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            binding.inputEmail.error = getString(R.string.email_cannot_be_empty)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.error = getString(R.string.invalid_email_format)
            return false
        }

        if (password.isEmpty()) {
            binding.inputPassword.error = getString(R.string.password_cannot_be_empty)
            return false
        }

        if (password.length < 8) {
            binding.inputPassword.error =
                getString(R.string.password_must_be_at_least_8_characters_long)
            return false
        }

        if (password != confirmPassword) {
            binding.inputConfirmPassword.error = getString(R.string.passwords_do_not_match)
            return false
        }

        return true
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