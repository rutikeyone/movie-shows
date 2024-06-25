package com.ru.movieshows.tv_shows.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.tv_shows.domain.entities.Review

class ReviewsDiffItemCallback : DiffUtil.ItemCallback<Review>() {

    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}

