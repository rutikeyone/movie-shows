package com.ru.movieshows.app.presentation.adapters.movies

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.sources.movies.entities.MovieEntity

class MoviesDiffItemCallback :  DiffUtil.ItemCallback<MovieEntity>() {

    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem == newItem
    }

}