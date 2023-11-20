package com.ru.movieshows.presentation.adapters

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.ReviewItemBinding
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.presentation.adapters.diff_callback.ReviewsDiffCallback

class ReviewsListAdapter: PagingDataAdapter<ReviewEntity, ReviewsListAdapter.Holder>(ReviewsDiffCallback()) {

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
            renderRating(review)
            renderAvatarImageView(review)
            renderContent(review)
        }

        private fun ReviewItemBinding.renderContent(review: ReviewEntity) {
            if (review.content != null) {
                reviewTextView.text = review.content
                reviewTextView.movementMethod = LinkMovementMethod.getInstance()
            }
        }

        private fun ReviewItemBinding.renderRating(
            review: ReviewEntity,
        ) {
            if (review.authorDetails?.rating != null && review.authorDetails.rating > 2) {
                val value = review.authorDetails.rating.toFloat() / 2
                ratingBar.rating = value
            } else {
                ratingBar.visibility = View.GONE
            }
        }

        private fun ReviewItemBinding.renderAvatarImageView(
            review: ReviewEntity,
        ) {
            if (review.authorDetails?.avatar != null) {
                Glide
                    .with(binding.root)
                    .load(review.authorDetails.avatar)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(avatarImageView)
            } else {
                avatarImageView.visibility = View.GONE
            }
        }

    }
}