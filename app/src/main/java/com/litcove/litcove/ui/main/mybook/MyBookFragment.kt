package com.litcove.litcove.ui.main.mybook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.litcove.litcove.databinding.FragmentMyBookBinding

class MyBookFragment : Fragment() {

    private var _binding: FragmentMyBookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myBookViewModel =
            ViewModelProvider(this)[MyBookViewModel::class.java]

        _binding = FragmentMyBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tabLayout: TabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        val sections = arrayOf("History", "Collection")

        viewPager.adapter = SectionPagerAdapter(this, sections)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = sections[position]
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}