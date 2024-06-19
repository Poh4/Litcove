package com.litcove.litcove.ui.main.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.FragmentSearchBinding
import com.litcove.litcove.utils.CenteredGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnBookClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchInput.requestFocus()

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                viewModel.findBooks(query, 0, 10)
            }
        })

        val searchAdapter = SearchAdapter(this)
        val displayMetrics = resources.displayMetrics
        val totalWidth = displayMetrics.widthPixels
        val itemWidth = totalWidth / 2
        val numColumns = totalWidth / itemWidth
        val layoutManager = GridLayoutManager(context, numColumns)
        binding.recyclerViewSearchResults.layoutManager = layoutManager
        binding.recyclerViewSearchResults.addItemDecoration(CenteredGridItemDecoration(totalWidth, numColumns, itemWidth))
        binding.recyclerViewSearchResults.adapter = searchAdapter

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            if (searchResults.isNotEmpty()) {
                binding.recyclerViewSearchResults.visibility = View.VISIBLE
                searchAdapter.setBooks(searchResults)
            } else {
                binding.recyclerViewSearchResults.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBookClick(book: Book) {
        val action = SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment(book)
        try {
            view?.findNavController()?.navigate(action)
        } catch (e: Exception) {
            Log.e("SearchFragment", "Failed to navigate to book details: $e")
        }
    }
}