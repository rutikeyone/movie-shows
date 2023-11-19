package com.ru.movieshows.presentation.adapters

import android.text.Html
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.databinding.CommentItemBinding
import com.ru.movieshows.domain.entity.CommentEntity
import com.ru.movieshows.domain.entity.CommentLevelSnippetEntity
import com.ru.movieshows.presentation.adapters.diff_callback.CommentsDiffCallback

class CommentsListAdapter : PagingDataAdapter<CommentEntity, CommentsListAdapter.Holder>(CommentsDiffCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = getItem(position) ?: return
        holder.bind(comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        private val binding: CommentItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentEntity) = with(binding) {
            val commentSnippet = comment.snippet?.topLevelComment?.snippet
            bindUserName(commentSnippet)
            bindTimeAge(commentSnippet)
            bindComment(commentSnippet)
            bindCountReplies(commentSnippet)
        }

        private fun bindCountReplies(commentSnippet: CommentLevelSnippetEntity?) = with(binding) {
            countRepliesButton.isVisible = false
        }

        private fun bindComment(commentSnippet: CommentLevelSnippetEntity?) = with(binding) {
            val textDisplay = commentSnippet?.textDisplay
            if(!textDisplay.isNullOrEmpty()) {
                val value = Html.fromHtml(textDisplay)
                commentTextView.text = textDisplay
            } else {
                commentTextView.isVisible = false
            }
        }

        private fun bindTimeAge(commentSnippet: CommentLevelSnippetEntity?) = with(binding) {
            val publishedAt = commentSnippet?.publishedAt
            if(publishedAt != null) {
                val time: Long = publishedAt.time
                val now = System.currentTimeMillis()
                val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                timeAgoTextView.text = ago.toString()
            } else {
                timeAgoTextView.isVisible = false
            }
        }

        private fun bindUserName(comment: CommentLevelSnippetEntity?) = with(binding) {
            val authorDisplayName = comment?.authorDisplayName
            if(!authorDisplayName.isNullOrEmpty()) {
                userTextView.text = authorDisplayName
            } else {
                userTextView.isVisible = false
            }
        }
    }

}