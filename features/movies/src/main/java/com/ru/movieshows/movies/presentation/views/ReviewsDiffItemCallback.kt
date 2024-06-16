package com.ru.movieshows.movies.presentation.views

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.movies.domain.entities.Review

class ReviewsDiffItemCallback : DiffUtil.ItemCallback<Review>() {

    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}

