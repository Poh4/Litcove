package com.litcove.litcove.ui.main.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.ItemSearchBinding

class SearchAdapter(private val listener : OnBookClickListener) : ListAdapter<Book, SearchAdapter.BookViewHolder>(BookDiffCallback()) {

    interface OnBookClickListener {
        fun onBookClick(book: Book)
    }

    class BookViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, listener: OnBookClickListener) {
            Glide.with(binding.imageViewSearch.context)
                .load(book.thumbnail)
                .into(binding.imageViewSearch)
            binding.textViewTitle.text = book.title
            binding.textViewAuthor.text = book.authors.joinToString(", ")

            itemView.setOnClickListener {
                listener.onBookClick(book)
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book, listener)
    }
}