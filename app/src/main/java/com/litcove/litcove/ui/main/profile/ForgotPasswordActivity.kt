package com.litcove.litcove.ui.main.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

            if (email == currentUserEmail) {
                if (validateInput()) {
                    viewModel.sendPasswordResetEmail(
                        email,
                        onSuccess = {
                            Toast.makeText(this,
                                getString(R.string.password_reset_email_sent), Toast.LENGTH_SHORT).show()
                            finish()
                        },
                        onFailure = {
                            Toast.makeText(this,
                                getString(R.string.failed_to_send_password_reset_email), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            } else {
                Toast.makeText(this,
                    getString(R.string.email_does_not_match_current_user_s_email), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        if (binding.inputEmail.text.toString().isEmpty()) {
            binding.inputLayoutEmail.error = getString(R.string.email_cannot_be_empty)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString()).matches()) {
            binding.inputLayoutEmail.error = getString(R.string.invalid_email_format)
            return false
        }
        return true
    }
}