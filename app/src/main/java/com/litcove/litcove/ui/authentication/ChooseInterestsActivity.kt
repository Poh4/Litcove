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

        viewModel.fetchInterests()
        viewModel.interests.observe(this) { interests ->
            for (interest in interests) {
                val checkBox = binding.gridLayout.findViewWithTag<CheckBox>(interest)
                checkBox?.isChecked = true
            }
        }

        binding.gridLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.gridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val gridLayoutWidth = binding.gridLayout.measuredWidth
                val paddingInDp = 16
                val paddingInPx = (paddingInDp * resources.displayMetrics.density).toInt()
                val cardViewHeightInDp = 80
                val cardViewHeightInPx = (cardViewHeightInDp * resources.displayMetrics.density).toInt()

                for (category in categories) {
                    val checkBox = CheckBox(this@ChooseInterestsActivity).apply {
                        text = category
                        tag = category
                        textSize = 16f
                    }

                    val cardView = MaterialCardView(this@ChooseInterestsActivity).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            gridLayoutWidth / 2,
                            cardViewHeightInPx
                        )
                        radius = 16f
                        setContentPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)
                        addView(checkBox)
                    }

                    binding.gridLayout.columnCount = 2
                    binding.gridLayout.addView(cardView)
                }
            }
        })

        binding.buttonSubmit.setOnClickListener {
            val checkedCategories = categories.filter { category ->
                val checkBox = binding.gridLayout.findViewWithTag<CheckBox>(category)
                checkBox.isChecked
            }

            if (checkedCategories.isEmpty()) {
                Toast.makeText(this, getString(R.string.select_at_least_one), Toast.LENGTH_SHORT).show()
            } else {
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
}