package com.litcove.litcove.ui.main.explore

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class GenrePagerAdapter(fragment: Fragment, private val genres: Array<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = genres.size

    override fun createFragment(position: Int): Fragment {
        return GenreFragment()
    }
}