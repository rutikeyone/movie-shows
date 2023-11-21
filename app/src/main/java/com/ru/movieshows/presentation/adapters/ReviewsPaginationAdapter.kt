package com.ru.movieshows.presentation.adapters

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.ReviewItemBinding
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.presentation.adapters.diff_callback.ReviewsDiffItemCallback

class ReviewsPaginationAdapter: PagingDataAdapter<ReviewEntity, ReviewsPaginationAdapter.Holder>(ReviewsDiffItemCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = getItem(position) ?: return
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReviewItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        private val binding: ReviewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity) = with(binding) {
            if (review.author != null) reviewerHeaderView.text = review.author
            bindRating(review)
            bindAvatarImageView(review)
            bindContent(review)
        }

        private fun bindContent(review: ReviewEntity) = with(binding) {
            val content = review.content
            if(!content.isNullOrEmpty()) {
                reviewTextView.text = content
                reviewTextView.isVisible = true
                reviewTextView.movementMethod = LinkMovementMethod.getInstance()
            } else {
                reviewTextView.isVisible = false
            }
        }

        private fun bindRating(review: ReviewEntity, ) = with(binding.ratingBar) {
            val rating = review.authorDetails?.rating
            if (rating != null && rating > 2) {
                val value = review.authorDetails.rating.toFloat() / 2
                this.rating = value
                this.isVisible = true
            } else {
                this.isVisible = false
            }
        }

        private fun bindAvatarImageView(review: ReviewEntity) = with(binding.avatarImageView) {
            val backDrop = review.authorDetails?.avatar
            if (!backDrop.isNullOrEmpty()) {
                Glide
                    .with(this.context)
                    .load(backDrop)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            } else {
                Glide
                    .with(context)
                    .load(R.drawable.poster_placeholder_bg)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }
}