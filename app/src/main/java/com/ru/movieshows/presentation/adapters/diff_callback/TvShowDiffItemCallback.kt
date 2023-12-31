package com.ru.movieshows.presentation.adapters.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.domain.entity.TvShowsEntity

class TvShowDiffItemCallback : DiffUtil.ItemCallback<TvShowsEntity>() {
    override fun areItemsTheSame(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return oldItem == newItem
    }
}