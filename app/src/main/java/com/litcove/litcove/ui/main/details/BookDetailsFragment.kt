package com.litcove.litcove.ui.main.details

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.litcove.litcove.R
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.FragmentBookDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class BookDetailsFragment : Fragment() {
    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding

    private val viewModel: BookDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        val books: Book? = arguments?.getParcelable("book")

        displayBookDetails(books)

        binding?.descriptionCardView?.setOnClickListener {
            binding?.descriptionTextView?.maxLines = if (binding?.descriptionTextView?.maxLines == 5) 1000 else 5
        }

        return binding?.root
    }

    private fun displayBookDetails(books: Book?) {
        books?.let { book ->
            binding?.apply {
                titleTextView.text = book.title
                authorTextView.text = book.authors.joinToString(", ")
                descriptionTextView.text = book.description

                Glide.with(this@BookDetailsFragment)
                    .load(book.thumbnail)
                    .into(coverImageView)

                addBookToHistory(book)

                viewModel.isBookInCollection(book.bookId,
                    onExist = {
                        buttonAddToCollection.setIconResource(R.drawable.ic_bookmark_filled)
                        buttonAddToCollection.text = getString(R.string.added_to_collection)
                        buttonAddToCollection.setOnClickListener { removeBookFromCollection(book) }
                    },
                    onNotExist = {
                        buttonAddToCollection.setIconResource(R.drawable.ic_bookmark_outlined)
                        buttonAddToCollection.text = getString(R.string.add_to_collection)
                        buttonAddToCollection.setOnClickListener { addBookToCollection(book) }
                    },
                    onFailure = { e ->
                        Log.e("BookDetailsFragment",
                            getString(R.string.error_checking_book_in_collection), e)
                        Toast.makeText(context,
                            getString(R.string.error_checking_book_in_collection), Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.let {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        val toolbarTitle = activity?.findViewById<TextView>(R.id.toolbar_title)
        val params = toolbarTitle?.layoutParams as Toolbar.LayoutParams
        params.gravity = Gravity.CENTER
        toolbarTitle.layoutParams = params
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
        binding?.coverImageView?.let { Glide.with(this).clear(it) }
        _binding = null
        val toolbarTitle = activity?.findViewById<TextView>(R.id.toolbar_title)
        val params = toolbarTitle?.layoutParams as Toolbar.LayoutParams
        params.gravity = Gravity.START
        toolbarTitle.layoutParams = params
    }

    private fun addBookToHistory(book: Book) {
        viewModel.addBookToHistory(book,
            onSuccess = {
                Log.d("BookDetailsFragment", getString(R.string.added_to_history))
            },
            onFailure = { e ->
                Log.e("BookDetailsFragment", getString(R.string.error_adding_book_to_history), e)
            }
        )
    }

    private fun addBookToCollection(book: Book) {
        viewModel.addBookToCollection(book,
            onSuccess = {
                binding?.buttonAddToCollection?.setIconResource(R.drawable.ic_bookmark_filled)
                binding?.buttonAddToCollection?.text = getString(R.string.added_to_collection)
                binding?.buttonAddToCollection?.setOnClickListener { removeBookFromCollection(book) }
                Toast.makeText(context,
                    getString(R.string.added_to_collection), Toast.LENGTH_SHORT).show()
            },
            onFailure = { e ->
                Log.e("BookDetailsFragment", getString(R.string.error_adding_book_to_collection), e)
                Toast.makeText(context,
                    getString(R.string.error_adding_book_to_collection), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun removeBookFromCollection(book: Book) {
        viewModel.removeBookFromCollection(book.bookId,
            onSuccess = {
                binding?.buttonAddToCollection?.setIconResource(R.drawable.ic_bookmark_outlined)
                binding?.buttonAddToCollection?.text = getString(R.string.add_to_collection)
                binding?.buttonAddToCollection?.setOnClickListener { addBookToCollection(book) }
                Toast.makeText(context,
                    getString(R.string.removed_from_collection), Toast.LENGTH_SHORT).show()
            },
            onFailure = { e ->
                Log.e("BookDetailsFragment", getString(R.string.error_removing_book_from_collection), e)
                Toast.makeText(context,
                    getString(R.string.error_removing_book_from_collection), Toast.LENGTH_SHORT).show()
            }
        )
    }
}