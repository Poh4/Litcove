package com.litcove.litcove.ui.main.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityChangePasswordBinding
import com.litcove.litcove.utils.LoadingDialog

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingDialog = LoadingDialog(this)

        binding.buttonSubmit.setOnClickListener {
            if (validateInput()) {
                loadingDialog.startLoading()
                viewModel.changePassword(
                    binding.inputOldPassword.text.toString(),
                    binding.inputNewPassword.text.toString(),
                    onSuccess = {
                        Toast.makeText(this, getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = {
                        Toast.makeText(this,
                            getString(R.string.failed_to_change_password), Toast.LENGTH_SHORT).show()
                        loadingDialog.stopLoading()
                    }
                )
            }
        }
    }

    private fun validateInput(): Boolean {
        val oldPassword = binding.inputOldPassword.text.toString()
        val newPassword = binding.inputNewPassword.text.toString()
        val confirmPassword = binding.inputConfirmPassword.text.toString()

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            binding.inputLayoutOldPassword.error = getString(R.string.password_cannot_be_empty)
            binding.inputLayoutNewPassword.error = getString(R.string.password_cannot_be_empty)
            binding.inputConfirmPassword.error = getString(R.string.password_cannot_be_empty)
            return false
        }

        if(newPassword == oldPassword) {
            binding.inputLayoutNewPassword.error = getString(R.string.new_password_must_be_different)
            return false
        }

        if(oldPassword.length < 8) {
            binding.inputLayoutOldPassword.error = getString(R.string.password_must_be_at_least_8_characters_long)
            return false
        }

        if(newPassword.length < 8) {
            binding.inputLayoutNewPassword.error = getString(R.string.password_must_be_at_least_8_characters_long)
            return false
        }

        if (newPassword != confirmPassword) {
            binding.inputLayoutConfirmPassword.error = getString(R.string.passwords_do_not_match)
            return false
        }

        return true
    }
}