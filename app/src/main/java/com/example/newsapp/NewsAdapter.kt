package com.example.newsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(private val news : ArrayList<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false) // Ubah bagian ini
        return NewsViewHolder(binding)
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position])
    }
    override fun getItemCount() = news.size
    class NewsViewHolder(val binding : ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsItem) {
            binding.tvTitle.text = news.title
            binding.tvSubtitle1.text = news.author
            binding.tvSubtitle2.text = news.publishedAt
            Glide.with(binding.root)
                .load(news.urlToImage)
                .into(binding.ivIcon)
        }
    }
}