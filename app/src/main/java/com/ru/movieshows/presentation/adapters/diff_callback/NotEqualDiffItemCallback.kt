package com.ru.movieshows.presentation.adapters.diff_callback

import androidx.recyclerview.widget.DiffUtil

class NotEqualDiffItemCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean = false

    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean = false
}