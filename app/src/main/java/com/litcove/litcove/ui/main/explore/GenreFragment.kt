package com.litcove.litcove.ui.main.explore

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.litcove.litcove.databinding.FragmentGenreBinding
import com.litcove.litcove.utils.VerticalSpacingItemDecoration
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GenreFragment : Fragment() {
    val viewModel: GenreViewModel by viewModels()
    private var _binding: FragmentGenreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val genre = arguments?.getString("genre")
        if (genre != null) {
            viewModel.setSubject(genre)
        } else {
            Log.e("GenreFragment", "No genre argument provided")
        }

        val genreAdapter = GenreAdapter()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.booksFlow.collectLatest { pagingData ->
                genreAdapter.submitData(pagingData)
            }
        }

        binding.recyclerViewGenre.adapter = genreAdapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter { genreAdapter.retry() },
            footer = LoadingStateAdapter { genreAdapter.retry() }
        )

        val verticalSpacingItemDecoration = VerticalSpacingItemDecoration(dpToPx(requireContext()))
        binding.recyclerViewGenre.addItemDecoration(verticalSpacingItemDecoration)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel any ongoing operations
        viewLifecycleOwner.lifecycleScope.cancel()
        _binding = null
    }

    private fun dpToPx(context: Context): Int {
        return (4 * context.resources.displayMetrics.density).toInt()
    }
}