package com.ru.movieshows.presentation.adapters.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.ru.movieshows.domain.entity.CommentEntity

class CommentsDiffCallback : DiffUtil.ItemCallback<CommentEntity>()  {
    override fun areItemsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
        return oldItem == newItem
    }
}