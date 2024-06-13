package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.databinding.ActivityChooseInterestsBinding
import com.litcove.litcove.ui.main.MainActivity

class ChooseInterestsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityChooseInterestsBinding
    private val viewModel: ChooseInterestsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityChooseInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = auth.currentUser?.uid

        val categories = arrayOf("Comedy", "Science", "Comic", "Fiction", "Non-fiction", "Biography", "Autobiography", "Fantasy", "Horror", "Mystery", "Romance", "Science Fiction", "History", "Humor", "Adventure", "Thriller", "Drama", "Poetry", "Encyclopedia", "Dictionary", "Cookbook", "Children's", "Young Adult", "Adult", "Educational", "Reference", "Art", "Music", "Sports", "Travel")
        for (category in categories) {
            val checkBox = CheckBox(this).apply {
                text = category
                tag = category
                textSize = 16f
            }
            binding.gridLayout.addView(checkBox)
        }

        if (userId != null) {
            viewModel.checkInterests(userId)

            binding.buttonSkip.setOnClickListener {
                viewModel.updateInterests(userId, listOf())
            }

            binding.buttonSubmit.setOnClickListener {
                val checkedCategories = categories.filter { category ->
                    val checkBox = binding.gridLayout.findViewWithTag<CheckBox>(category)
                    checkBox.isChecked
                }

                viewModel.updateInterests(userId, checkedCategories)
            }
        } else {
            startActivity(Intent(this@ChooseInterestsActivity, LoginActivity::class.java))
            finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.hasInterests.observe(this) { hasInterests ->
            if (hasInterests) {
                startActivity(Intent(this@ChooseInterestsActivity, MainActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this@ChooseInterestsActivity, "Error: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.updateSuccess.observe(this) { success ->
            if (success) {
                startActivity(Intent(this@ChooseInterestsActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}