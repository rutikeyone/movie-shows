package com.ru.movieshows.presentation.adapters

import android.text.Html
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.databinding.CommentDetailsItemBinding
import com.ru.movieshows.domain.entity.CommentLevelEntity
import com.ru.movieshows.domain.entity.CommentLevelSnippetEntity
import com.ru.movieshows.presentation.utils.OnTouchListener

class CommentsAdapter(
    private val comments: ArrayList<CommentLevelEntity>,
) : RecyclerView.Adapter<CommentsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentDetailsItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    inner class Holder(
        private val binding: CommentDetailsItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentLevelEntity) = with(binding) {
            val commentSnippet = comment.snippet
            binding.root.background = null
            bindUserName(commentSnippet)
            bindTimeAge(commentSnippet)
            bindComment(commentSnippet)
        }

        private fun bindComment(commentSnippet: CommentLevelSnippetEntity?) = with(binding.commentTextView) {
            val textDisplay = commentSnippet?.textDisplay
            if(!textDisplay.isNullOrEmpty()) {
                val value = Html.fromHtml(textDisplay, HtmlCompat.FROM_HTML_MODE_LEGACY)
                val onTouchListener = OnTouchListener()
                text = value
                isVisible = true
                setOnTouchListener(onTouchListener)
            } else {
                isVisible = false
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