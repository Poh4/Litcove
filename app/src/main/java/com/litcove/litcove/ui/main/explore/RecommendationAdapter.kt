package com.litcove.litcove.ui.main.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.data.model.Book
import com.litcove.litcove.databinding.ItemRecommendationBinding

class RecommendationAdapter(private var recommendations: List<Book>, private val listener : OnBookClickListener) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    interface OnBookClickListener {
        fun onBookClick(book: Book)
    }

    class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, listener: OnBookClickListener) {
            Glide.with(binding.imageViewRecommendation.context)
                .load(book.thumbnail)
                .into(binding.imageViewRecommendation)
            binding.textViewTitle.text = book.title
            binding.textViewAuthor.text = book.authors.joinToString(", ")

            itemView.setOnClickListener {
                listener.onBookClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecommendationBinding.inflate(layoutInflater, parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(recommendations[position], listener)
    }

    override fun getItemCount(): Int {
        return recommendations.size
    }

    fun updateData(newBooks: List<Book>) {
        recommendations = newBooks
        notifyItemChanged(0, recommendations.size)
    }
}