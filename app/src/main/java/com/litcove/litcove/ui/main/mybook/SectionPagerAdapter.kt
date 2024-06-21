package com.litcove.litcove.ui.main.mybook

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(fragment: Fragment, private val genres: Array<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = genres.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryFragment()
            1 -> CollectionFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}