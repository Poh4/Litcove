package com.litcove.litcove.ui.main.explore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.litcove.litcove.databinding.FragmentExploreBinding
import com.litcove.litcove.utils.HorizontalSpacingItemDecoration

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var currentGenreFragment: GenreFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exploreViewModel =
            ViewModelProvider(this)[ExploreViewModel::class.java]

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recommendations = mutableListOf<String>()
        for (i in 1..10) {
            recommendations.add("https://marketplace.canva.com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg")
        }
        val recommendationAdapter = RecommendationAdapter(recommendations)
        binding.recyclerViewRecommendation.adapter = recommendationAdapter

        val horizontalSpacingItemDecoration = HorizontalSpacingItemDecoration(dpToPx(requireContext()))
        binding.recyclerViewRecommendation.addItemDecoration(horizontalSpacingItemDecoration)

        val tabLayout: TabLayout = binding.tabLayout
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentGenreFragment = (viewPager.adapter as GenrePagerAdapter).createFragment(position) as GenreFragment
                if (currentGenreFragment?.isAdded == true) {
                    currentGenreFragment?.viewModel?.refresh()
                }
            }
        })

        exploreViewModel.interests.observe(viewLifecycleOwner) { interests ->
            viewPager.adapter = GenrePagerAdapter(requireActivity(), interests.toTypedArray())
            viewPager.offscreenPageLimit = interests.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = interests[position]
            }.attach()
        }

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