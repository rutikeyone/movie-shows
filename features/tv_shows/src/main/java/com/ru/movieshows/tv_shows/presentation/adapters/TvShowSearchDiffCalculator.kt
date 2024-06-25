package com.ru.movieshows.tv_shows.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch

class TvShowSearchDiffCalculator : DiffUtil.ItemCallback<TvShowSearch>() {
    override fun areItemsTheSame(oldItem: TvShowSearch, newItem: TvShowSearch): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TvShowSearch, newItem: TvShowSearch): Boolean {
        return oldItem == newItem
    }

}