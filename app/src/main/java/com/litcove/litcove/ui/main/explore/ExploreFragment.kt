package com.litcove.litcove.ui.main.explore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.litcove.litcove.databinding.FragmentExploreBinding
import com.litcove.litcove.utils.HorizontalSpacingItemDecoration

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exploreViewModel =
            ViewModelProvider(this)[ExploreViewModel::class.java]

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tabLayout: TabLayout = binding.tabLayout
        val genres = arrayOf("Romance", "Comedy", "Fiction", "Horror")

        for (genre in genres) {
            tabLayout.addTab(tabLayout.newTab().setText(genre))
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Ganti genre berdasarkan tab yang dipilih
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Opsional: lakukan sesuatu saat tab tidak lagi dipilih
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Opsional: lakukan sesuatu saat tab dipilih kembali
            }
        })

        val recommendations = mutableListOf<String>()
        for (i in 1..10) {
            recommendations.add("https://marketplace.canva.com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg")
        }
        val recommendationAdapter = RecommendationAdapter(recommendations)
        binding.recyclerViewRecommendation.adapter = recommendationAdapter

        val horizontalSpacingItemDecoration = HorizontalSpacingItemDecoration(dpToPx(requireContext()))
        binding.recyclerViewRecommendation.addItemDecoration(horizontalSpacingItemDecoration)

        return root
    }

    private fun dpToPx(context: Context): Int {
        return (8 * context.resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}