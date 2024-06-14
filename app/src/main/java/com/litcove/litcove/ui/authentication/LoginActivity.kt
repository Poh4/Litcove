package com.litcove.litcove.ui.authentication

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.databinding.ActivityLoginBinding
import com.litcove.litcove.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadThemeSetting()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLoginGoogle.setOnClickListener {
            GoogleAuthUtils.registerWithGoogle(this, auth, lifecycleScope) {}
        }

        val registerPrompt = binding.textViewRegisterPrompt
        registerPrompt.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onStart() {
        Log.d("LoginActivity", "onStart called")
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

        if (auth.currentUser != null) {
            auth.currentUser?.let {
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

    private fun saveInitialThemeSetting() {
        val uiModeManager = this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val initialTheme = when (uiModeManager.nightMode) {
            UiModeManager.MODE_NIGHT_YES -> true
            UiModeManager.MODE_NIGHT_NO -> false
            else -> this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
        viewModel.saveInitialThemeSetting(initialTheme)
    }
}