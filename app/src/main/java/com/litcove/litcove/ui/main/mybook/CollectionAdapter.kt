package com.litcove.litcove.ui.main.mybook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.databinding.ItemCollectionBinding

class CollectionAdapter(private val books: List<String>) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    class CollectionViewHolder(private val binding: ItemCollectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: String) {
            Glide.with(binding.imageViewCollection.context)
                .load(recommendation)
                .into(binding.imageViewCollection)
            binding.textViewTitle.text = "Soul"
            binding.textViewAuthor.text = "Olivia Wilson"
            binding.textViewSummary.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCollectionBinding.inflate(layoutInflater, parent, false)
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size
}