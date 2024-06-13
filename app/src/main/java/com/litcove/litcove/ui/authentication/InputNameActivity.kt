package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.databinding.ActivityInputNameBinding
import java.util.Locale

class InputNameActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityInputNameBinding
    private val viewModel: InputNameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityInputNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            viewModel.checkIfNameExists(userId,
                onExists = {
                    startActivity(Intent(this@InputNameActivity, ChooseInterestsActivity::class.java))
                },
                onFailure = { exception ->
                    Log.d("InputNameActivity", "Error checking if name exists: $exception")
                }
            )
        }

        binding.buttonSubmit.setOnClickListener {
            if (userId != null) {
                val fullName = binding.inputFullName.text.toString().lowercase(Locale.getDefault())
                val titleCaseName = fullName.split(" ").joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
                viewModel.saveNameToFirestore(userId, titleCaseName,
                    onSuccess = {
                        Toast.makeText(this, "Name saved", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { e ->
                        Toast.makeText(this, "Error saving name: $e", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            startActivity(Intent(this@InputNameActivity, LoginActivity::class.java))
        }
    }
}