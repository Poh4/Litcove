package com.litcove.litcove.ui.main.mybook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litcove.litcove.databinding.FragmentHistoryBinding
import com.litcove.litcove.utils.VerticalSpacingItemDecoration

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val books = mutableListOf<String>()
        for (i in 1..10) {
            books.add("https://marketplace.canva.com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg")
        }
        val historyAdapter = HistoryAdapter(books)
        binding.recyclerViewHistory.adapter = historyAdapter

        val verticalSpacingItemDecoration = VerticalSpacingItemDecoration(dpToPx(requireContext()))
        binding.recyclerViewHistory.addItemDecoration(verticalSpacingItemDecoration)

        return root
    }

    private fun dpToPx(context: Context): Int {
        return (4 * context.resources.displayMetrics.density).toInt()
    }
}