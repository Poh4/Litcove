package com.litcove.litcove.ui.authentication

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityLoginBinding
import com.litcove.litcove.ui.main.MainActivity
import com.litcove.litcove.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val auth = Firebase.auth
    private var currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loadingDialog = LoadingDialog(this)

        viewModel.loadThemeSetting()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            if (validateInput(email, password)) {
                loadingDialog.startLoading()
                viewModel.loginWithEmail(email, password,
                    onSuccess = { user ->
                        currentUser = user
                        isUserLoggedIn()
                        Toast.makeText(this,
                            getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(this,
                            getString(R.string.error_logging_in), Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "Error logging in: $exception")
                        loadingDialog.stopLoading()
                    }
                )
            }
        }

        binding.buttonLoginGoogle.setOnClickListener {
            lifecycleScope.launch {
                suspendCoroutine { continuation ->
                    GoogleAuthUtils.registerWithGoogle(this@LoginActivity, auth, lifecycleScope) {
                        viewModel.registerUserToFirestore(it)
                        continuation.resume(Unit)
                    }
                }
                currentUser = auth.currentUser
                isUserLoggedIn()
            }
        }

        val registerPrompt = binding.textViewRegisterPrompt
        registerPrompt.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onStart() {
        super.onStart()
        viewModel.isDarkMode.observe(this) { isDarkMode ->
            when (isDarkMode) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

        isUserLoggedIn()
    }

    private fun saveInitialThemeSetting() {
        val uiModeManager = this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val initialTheme = when (uiModeManager.nightMode) {
            UiModeManager.MODE_NIGHT_YES -> true
            UiModeManager.MODE_NIGHT_NO -> false
            else -> this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
        viewModel.saveInitialThemeSetting(initialTheme)
    }

    private fun isUserLoggedIn() {
        if (currentUser != null) {
            currentUser?.let {
                viewModel.checkIfInterestsExists(it.uid,
                    onExists = {
                        saveInitialThemeSetting()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    },
                    onNotExists = {
                        saveInitialThemeSetting()
                        startActivity(Intent(this@LoginActivity, InputNameActivity::class.java))
                        finish()
                    },
                    onFailure = { exception ->
                        Log.d("LoginActivity", "Error checking if interests exist: $exception")
                    }
                )
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
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

        return true
    }
}