package com.litcove.litcove.ui.main.mybook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.ItemHistoryBinding

class HistoryAdapter(private val books: List<Book>, private val onBookClick: (Book) -> Unit) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(private val binding: ItemHistoryBinding, private val onBookClick: (Book) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            Glide.with(binding.imageViewHistory.context)
                .load(book.thumbnail)
                .into(binding.imageViewHistory)
            binding.textViewTitle.text = book.title
            binding.textViewAuthor.text = book.authors.joinToString(", ")
            binding.textViewSummary.text = book.description

            itemView.setOnClickListener {
                onBookClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(layoutInflater, parent, false)
        return HistoryViewHolder(binding, onBookClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size
}