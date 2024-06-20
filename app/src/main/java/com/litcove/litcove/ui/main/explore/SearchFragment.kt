package com.litcove.litcove.ui.main.explore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.litcove.litcove.R
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.FragmentSearchBinding
import com.litcove.litcove.utils.CenteredGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnBookClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private var toastAlreadyShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.let {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.searchInput.requestFocus()

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                viewModel.findBooks(query, 0, 10,
                    onExist = { books ->
                        binding.recyclerViewSearchResults.adapter?.let { adapter ->
                            if (adapter is SearchAdapter) {
                                adapter.submitList(books)
                            }
                            if (toastAlreadyShown == true) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.no_books_found), Toast.LENGTH_SHORT
                                ).show()
                                toastAlreadyShown = false
                            }
                        }
                    },
                    onNotExist = { books ->
                        binding.recyclerViewSearchResults.adapter?.let { adapter ->
                            if (adapter is SearchAdapter) {
                                adapter.submitList(books)
                            }
                        }
                        if (toastAlreadyShown == false) {
                            Toast.makeText(context, getString(R.string.no_books_found), Toast.LENGTH_SHORT).show()
                            toastAlreadyShown = true
                        }
                    },
                    onFailure = { throwable ->
                        Log.e("SearchFragment", "Failed to find books: $throwable")
                    }
                )
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
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
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