package com.ru.movieshows.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.domain.entity.TvShowsEntity

class TvShowDiffCallback : DiffUtil.ItemCallback<TvShowsEntity>() {
    override fun areItemsTheSame(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return false
    }
}