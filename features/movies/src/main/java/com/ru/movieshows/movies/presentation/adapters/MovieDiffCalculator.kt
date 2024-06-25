package com.ru.movieshows.movies.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.movies.domain.entities.Movie

class MovieDiffCalculator : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}