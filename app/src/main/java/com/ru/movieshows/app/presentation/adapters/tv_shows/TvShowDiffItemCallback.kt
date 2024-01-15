package com.ru.movieshows.app.presentation.adapters.tv_shows

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

class TvShowDiffItemCallback : DiffUtil.ItemCallback<TvShowsEntity>() {

    override fun areItemsTheSame(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return oldItem == newItem
    }

}