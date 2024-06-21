package com.litcove.litcove.ui.main.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.ItemGenreBinding

class GenreAdapter(private val listener: OnBookClickListener) : PagingDataAdapter<Book, GenreAdapter.GenreViewHolder>(DIFF_CALLBACK) {

    interface OnBookClickListener {
        fun onBookClick(book: Book)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.bookId == newItem.bookId
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class GenreViewHolder(private val binding: ItemGenreBinding, private val adapter: GenreAdapter) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = adapter.getItem(position)
                    if (item != null) {
                        listener.onBookClick(item)
                    }
                }
            }
        }

        fun bind(book: Book) {
            Glide.with(binding.imageViewGenre.context)
                .load(book.thumbnail)
                .into(binding.imageViewGenre)
            binding.textViewTitle.text = book.title
            binding.textViewAuthor.text = book.authors.joinToString(", ")
            binding.textViewSummary.text = book.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreBinding.inflate(layoutInflater, parent, false)
        return GenreViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val book = getItem(position)
        if (book != null) {
            holder.bind(book)
        }
    }
}