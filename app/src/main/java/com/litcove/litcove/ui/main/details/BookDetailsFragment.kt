package com.litcove.litcove.ui.main.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.FragmentBookDetailsBinding

class BookDetailsFragment : Fragment() {
    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        val book: Book? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("book", Book::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("book")
        }

        book?.let {
            binding.titleTextView.text = it.title
            binding.authorTextView.text = it.authors.joinToString(", ")
            binding.descriptionTextView.text = it.description

            Glide.with(this)
                .load(it.imageLinks.thumbnail)
                .into(binding.coverImageView)
        }

        binding.descriptionTextView.setOnClickListener {
            binding.descriptionTextView.maxLines = if (binding.descriptionTextView.maxLines == 5) Int.MAX_VALUE else 5
        }

        binding.buttonAddToCollection.setOnClickListener {

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}