package com.litcove.litcove.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.R
import com.litcove.litcove.data.constants.Categories
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

        val categories = Categories.bookCategories

        binding.gridLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to ensure it's only called once
                binding.gridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val gridLayoutWidth = binding.gridLayout.measuredWidth
                val paddingInDp = 16
                val paddingInPx = (paddingInDp * resources.displayMetrics.density).toInt()
                val cardViewHeightInDp = 80 // Set this to a fixed value that suits your needs
                val cardViewHeightInPx = (cardViewHeightInDp * resources.displayMetrics.density).toInt()

                for (category in categories) {
                    val checkBox = CheckBox(this@ChooseInterestsActivity).apply {
                        text = category
                        tag = category
                        textSize = 16f
                    }

                    val cardView = MaterialCardView(this@ChooseInterestsActivity).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            gridLayoutWidth / 2, // Set width to half of GridLayout's width
                            cardViewHeightInPx // Set height to a fixed value
                        )
                        radius = 16f
                        setContentPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx) // Set content padding to 16dp
                        addView(checkBox)
                    }

                    binding.gridLayout.columnCount = 2 // Set column count to 2
                    binding.gridLayout.addView(cardView)
                }
            }
        })

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