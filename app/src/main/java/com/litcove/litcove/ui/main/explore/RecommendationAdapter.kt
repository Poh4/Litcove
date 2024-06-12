package com.litcove.litcove.ui.main.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litcove.litcove.databinding.ItemRecommendationBinding

class RecommendationAdapter(private val recommendations: List<String>) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: String) {
            Glide.with(binding.imageViewRecommendation.context)
                .load(recommendation)
                .into(binding.imageViewRecommendation)
            binding.textViewTitle.text = "Soul"
            binding.textViewAuthor.text = "Olivia Wilson"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecommendationBinding.inflate(layoutInflater, parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(recommendations[position])
    }

    override fun getItemCount(): Int {
        return recommendations.size
    }
}