package com.litcove.litcove.ui.main.profile

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityEditProfileBinding
import com.litcove.litcove.utils.LoadingDialog
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingDialog = LoadingDialog(this)

        viewModel.imageAvatar.observe(this) { imageUrl ->
            if (imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(R.drawable.ic_no_profile)
                    .into(binding.imageAvatar)
            } else {
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.imageAvatar)
            }
        }

        viewModel.fullName.observe(this) { fullName ->
            binding.inputFullName.setText(fullName)
        }

        var selectedImageUri: Uri? = null

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(this)
                    .load(it)
                    .into(binding.imageAvatar)
                selectedImageUri = it
            }
        }

        binding.buttonChangeAvatar.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.buttonSave.setOnClickListener {
            loadingDialog.startLoading()
            val fullName = binding.inputFullName.text.toString()
            viewModel.viewModelScope.launch {
                var imageUri = selectedImageUri

                // Compress the image before uploading
                if (imageUri != null) {
                    imageUri = withContext(Dispatchers.IO) {
                        val originalFile = contentResolver.openInputStream(imageUri!!)?.use { inputStream ->
                            val tempFile = File.createTempFile("temp", null, cacheDir)
                            tempFile.outputStream().use { fileOut ->
                                inputStream.copyTo(fileOut)
                            }
                            tempFile
                        }

                        if (originalFile != null) {
                            Compressor.compress(this@EditProfileActivity, originalFile) {
                                size(1024_000)
                            }.toUri()
                        } else {
                            null
                        }
                    }
                }

                viewModel.saveProfileData(imageUri, fullName)
                loadingDialog.stopLoading()
                Toast.makeText(this@EditProfileActivity,
                    getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}