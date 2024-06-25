package com.ru.movieshows.movies.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.movies.domain.entities.MovieSearch

class MovieSearchDiffCalculator : DiffUtil.ItemCallback<MovieSearch>() {
    override fun areItemsTheSame(oldItem: MovieSearch, newItem: MovieSearch): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieSearch, newItem: MovieSearch): Boolean {
        return oldItem == newItem
    }

}