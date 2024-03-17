package com.ru.movieshows.app.presentation.adapters.reviews

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.sources.movies.entities.ReviewEntity

class ReviewsDiffItemCallback: DiffUtil.ItemCallback<ReviewEntity>() {

    override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem == newItem
    }

}

