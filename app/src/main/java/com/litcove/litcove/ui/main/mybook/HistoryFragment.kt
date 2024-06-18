package com.litcove.litcove.ui.main.mybook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.litcove.litcove.databinding.FragmentHistoryBinding
import com.litcove.litcove.utils.VerticalSpacingItemDecoration

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.books.observe(viewLifecycleOwner) { books ->
            val historyAdapter = HistoryAdapter(books) { book ->
                val action = MyBookFragmentDirections.actionNavigationMyBookToDetailFragment(book)
                findNavController().navigate(action)
            }
            binding.recyclerViewHistory.adapter = historyAdapter
        }

        val verticalSpacingItemDecoration = VerticalSpacingItemDecoration(dpToPx(requireContext()))
        binding.recyclerViewHistory.addItemDecoration(verticalSpacingItemDecoration)

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.init()
    }

    private fun dpToPx(context: Context): Int {
        return (4 * context.resources.displayMetrics.density).toInt()
    }
}