package com.ru.movieshows.tv_shows.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.tv_shows.domain.entities.TvShow

class TvShowDiffCalculator: DiffUtil.ItemCallback<TvShow>() {
    override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
        return oldItem == newItem
    }

}