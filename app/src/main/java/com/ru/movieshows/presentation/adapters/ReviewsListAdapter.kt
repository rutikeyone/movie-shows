package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.databinding.ReviewTileBinding
import com.ru.movieshows.domain.entity.AuthorDetailsEntity
import com.ru.movieshows.domain.entity.ReviewEntity

class ReviewsListAdapter: PagingDataAdapter<ReviewEntity, ReviewsListAdapter.Holder>(ReviewsDiffCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = getItem(position) ?: return
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReviewTileBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        private val binding: ReviewTileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity) = with(binding) {
            if (review.author != null) reviewerHeaderView.text = review.author
            renderRating(review)
            if(review.content != null) reviewTextView.text = review.content
            renderAvatarImageView(review)
        }

        private fun ReviewTileBinding.renderRating(
            review: ReviewEntity,
        ) {
            if (review.authorDetails?.rating != null && review.authorDetails.rating > 2) {
                val value = review.authorDetails.rating.toFloat() / 2
                ratingBar.rating = value
            } else {
                ratingBar.visibility = View.GONE
            }
        }

        private fun ReviewTileBinding.renderAvatarImageView(
            review: ReviewEntity,
        ) {
            if (review.authorDetails?.avatar != null) {
                Glide
                    .with(binding.root)
                    .load(review.authorDetails.avatar)
                    .centerCrop()
                    .into(avatarImageView)
            } else {
                avatarImageView.visibility = View.GONE
            }
        }

    }
}