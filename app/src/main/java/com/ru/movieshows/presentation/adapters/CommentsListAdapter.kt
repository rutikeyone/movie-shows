package com.ru.movieshows.presentation.adapters

import android.text.Html
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.R
import com.ru.movieshows.databinding.CommentItemBinding
import com.ru.movieshows.domain.entity.CommentEntity
import com.ru.movieshows.domain.entity.CommentLevelSnippetEntity
import com.ru.movieshows.presentation.adapters.diff_callback.CommentsDiffItemCallback

typealias CommentOnDetailsTapListener = (CommentEntity) -> Unit

class CommentsListAdapter(
    private val listener: CommentOnDetailsTapListener,
) : PagingDataAdapter<CommentEntity, CommentsListAdapter.Holder>(CommentsDiffItemCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = getItem(position) ?: return
        holder.bind(comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentItemBinding.inflate(inflater, parent, false)
        return Holder(binding, listener)
    }

    class Holder(
        private val binding: CommentItemBinding,
        private val listener: CommentOnDetailsTapListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentEntity) = with(binding) {
            val commentSnippet = comment.snippet?.topLevelComment?.snippet
            bindUserName(commentSnippet)
            bindTimeAge(commentSnippet)
            bindComment(commentSnippet)
            bindCountReplies(comment)
            setOnClickListener(comment)
        }

        private fun setOnClickListener(comment: CommentEntity) = with(binding) {
            root.setOnClickListener {
                listener.invoke(comment)
            }
        }

        private fun bindCountReplies(comment: CommentEntity) = with(binding.countRepliesButton) {
            val countReplies = comment.countReplies
            if(countReplies != null) {
                val value = resources.getQuantityString(R.plurals.count_replies, countReplies, countReplies)
                text = value
                setOnClickListener { listener.invoke(comment) }
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindComment(commentSnippet: CommentLevelSnippetEntity?) = with(binding) {
            val textDisplay = commentSnippet?.textDisplay
            if(!textDisplay.isNullOrEmpty()) {
                val value = Html.fromHtml(textDisplay, HtmlCompat.FROM_HTML_MODE_LEGACY)
                commentTextView.text = value
                commentTextView.movementMethod = LinkMovementMethod.getInstance()
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