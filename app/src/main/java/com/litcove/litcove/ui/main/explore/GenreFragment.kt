package com.litcove.litcove.ui.main.explore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litcove.litcove.databinding.FragmentGenreBinding
import com.litcove.litcove.utils.TopSpacingItemDecoration

class GenreFragment : Fragment() {

    private lateinit var binding: FragmentGenreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val books = mutableListOf<String>()
        for (i in 1..10) {
            books.add("https://marketplace.canva.com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg")
        }
        val genreAdapter = GenreAdapter(books)
        binding.recyclerViewGenre.adapter = genreAdapter

        val topSpacingItemDecoration = TopSpacingItemDecoration(dpToPx(requireContext()))
        binding.recyclerViewGenre.addItemDecoration(topSpacingItemDecoration)

        return root
    }

    private fun dpToPx(context: Context): Int {
        return (8 * context.resources.displayMetrics.density).toInt()
    }
}