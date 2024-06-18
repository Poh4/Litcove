package com.litcove.litcove.ui.main.mybook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.ItemCollectionBinding

class CollectionAdapter(private val books: List<Book>, private val onBookClick: (Book) -> Unit) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    class CollectionViewHolder(private val binding: ItemCollectionBinding, private val onBookClick: (Book) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            Glide.with(binding.imageViewCollection.context)
                .load(book.thumbnail)
                .into(binding.imageViewCollection)
            binding.textViewTitle.text = book.title
            binding.textViewAuthor.text = book.authors.joinToString(", ")
            binding.textViewSummary.text = book.description

            itemView.setOnClickListener {
                onBookClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCollectionBinding.inflate(layoutInflater, parent, false)
        return CollectionViewHolder(binding, onBookClick)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size
}