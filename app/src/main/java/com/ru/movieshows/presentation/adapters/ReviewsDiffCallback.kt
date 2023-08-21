package com.ru.movieshows.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.domain.entity.ReviewEntity

class ReviewsDiffCallback: DiffUtil.ItemCallback<ReviewEntity>() {
    override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem.id != newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem != newItem
    }

}