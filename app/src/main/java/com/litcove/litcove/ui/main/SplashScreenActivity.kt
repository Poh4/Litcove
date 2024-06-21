package com.litcove.litcove.ui.main

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.databinding.ActivitySplashScreenBinding
import com.litcove.litcove.ui.authentication.InputNameActivity
import com.litcove.litcove.ui.authentication.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: ActivitySplashScreenBinding
    private var nextActivityIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isUserLoggedIn()

        binding.splashScreen.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Animation started.
            }

            override fun onAnimationEnd(animation: Animator) {
                // Animation ended.
                nextActivityIntent?.let {
                    startActivity(it)
                    finish()
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                // Animation cancelled.
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Animation repeated.
            }
        })
    }

    private fun isUserLoggedIn() {
        if (auth.currentUser != null) {
            auth.currentUser?.let {
                viewModel.checkIfInterestsExists(it.uid,
                    onExists = {
                        nextActivityIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    },
                    onNotExists = {
                        nextActivityIntent = Intent(this@SplashScreenActivity, InputNameActivity::class.java)
                    },
                    onFailure = { exception ->
                        Log.d("SplashScreenActivity", "Error checking if interests exist: $exception")
                    }
                )
            }
        } else {
            nextActivityIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        }
    }
}