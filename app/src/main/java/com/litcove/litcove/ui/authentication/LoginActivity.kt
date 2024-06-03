package com.litcove.litcove.ui.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.litcove.litcove.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = LoginViewModel()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}