package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.R
import com.litcove.litcove.databinding.ActivityChooseInterestsBinding
import com.litcove.litcove.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseInterestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseInterestsBinding
    private val viewModel: ChooseInterestsViewModel by viewModels()
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categories = arrayOf("Comedy", "Science", "Comic", "Fiction", "Non-fiction", "Biography", "Autobiography", "Fantasy", "Horror", "Mystery", "Romance", "Science Fiction", "History", "Humor", "Adventure", "Thriller", "Drama", "Poetry", "Encyclopedia", "Dictionary", "Cookbook", "Children's", "Young Adult", "Adult", "Educational", "Reference", "Art", "Music", "Sports", "Travel")
        for (category in categories) {
            val checkBox = CheckBox(this).apply {
                text = category
                tag = category
                textSize = 16f
            }
            binding.gridLayout.addView(checkBox)
        }

        binding.buttonSkip.setOnClickListener {
            auth.currentUser?.let { it1 -> viewModel.updateInterests(it1.uid, emptyList(),
                onSuccess = {
                    startActivity(Intent(this@ChooseInterestsActivity, MainActivity::class.java))
                    finish()
                },
                onFailure = { e ->
                    Toast.makeText(this,
                        getString(R.string.error_saving_interests), Toast.LENGTH_SHORT).show()
                    Log.e("ChooseInterestsActivity", "Error saving interests", e)
                }
            ) }
        }

        binding.buttonSubmit.setOnClickListener {
            val checkedCategories = categories.filter { category ->
                val checkBox = binding.gridLayout.findViewWithTag<CheckBox>(category)
                checkBox.isChecked
            }

            auth.currentUser?.let { it1 -> viewModel.updateInterests(it1.uid, checkedCategories,
                onSuccess = {
                    Toast.makeText(this, getString(R.string.interests_updated), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ChooseInterestsActivity, MainActivity::class.java))
                    finish()
                },
                onFailure = { e ->
                    Toast.makeText(this,
                        getString(R.string.error_saving_interests), Toast.LENGTH_SHORT).show()
                    Log.e("ChooseInterestsActivity", "Error saving interests", e)
                }
            ) }
        }
    }
}