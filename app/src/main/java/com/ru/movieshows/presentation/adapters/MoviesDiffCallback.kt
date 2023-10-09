package com.ru.movieshows.presentation.adapters

import androidx.paging.DifferCallback
import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity

class MoviesDiffCallback :  DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return false
    }
}