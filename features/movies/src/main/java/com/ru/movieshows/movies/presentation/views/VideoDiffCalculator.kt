package com.ru.movieshows.movies.presentation.views

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.movies.domain.entities.Video

class VideoDiffCalculator : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }

}