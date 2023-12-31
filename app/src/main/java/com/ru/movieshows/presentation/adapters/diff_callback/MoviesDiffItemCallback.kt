package com.ru.movieshows.presentation.adapters.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.domain.entity.MovieEntity

class MoviesDiffItemCallback :  DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem == newItem
    }
}