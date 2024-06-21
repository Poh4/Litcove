package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityInputNameBinding
import java.util.Locale

class InputNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputNameBinding
    private val viewModel: InputNameViewModel by viewModels()
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInputNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth.currentUser?.let {
            viewModel.checkIfNameExists(it.uid,
                onExists = {
                    startActivity(Intent(this@InputNameActivity, ChooseInterestsActivity::class.java))
                },
                onFailure = { exception ->
                    Log.d("InputNameActivity", "Error checking if name exists: $exception")
                }
            )
        }

        binding.buttonSubmit.setOnClickListener {
            val fullName = binding.inputFullName.text.toString().lowercase(Locale.getDefault())
            val titleCaseName = fullName.split(" ").joinToString(" ") { name -> name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
            auth.currentUser?.let { it1 ->
                viewModel.saveNameToFirestore(it1.uid, titleCaseName,
                    onSuccess = {
                        Toast.makeText(this, getString(R.string.name_saved), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@InputNameActivity, ChooseInterestsActivity::class.java))
                    },
                    onFailure = { e ->
                        Toast.makeText(this,
                            getString(R.string.error_saving_name), Toast.LENGTH_SHORT).show()
                        Log.e("InputNameActivity", "Error saving name", e)
                    }
                )
            }
        }
    }
}