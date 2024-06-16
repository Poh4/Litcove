package com.litcove.litcove.ui.main.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class GenrePagerAdapter(fa: FragmentActivity, private val genres: Array<String>) : FragmentStateAdapter(fa) {
    private val fragments = mutableMapOf<Int, GenreFragment>()

    override fun getItemCount(): Int = genres.size

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position] ?: GenreFragment().also {
            val args = Bundle()
            args.putString("genre", genres[position])
            it.arguments = args
            fragments[position] = it
        }
        return fragment
    }
}