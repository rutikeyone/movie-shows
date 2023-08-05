package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.databinding.ReviewTileBinding
import com.ru.movieshows.domain.entity.ReviewEntity

class ReviewsAdapter: PagingDataAdapter<ReviewEntity, ReviewsAdapter.Holder>(ReviewsDiffCallback()) {

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
            if(review.author != null) reviewerHeaderView.text = review.author
            if(review.authorDetails?.rating != null && review.authorDetails.rating > 2) {
                val value = review.authorDetails.rating.toFloat() / 2
                ratingBar.rating = value
            } else {
                ratingBar.visibility = View.GONE
            }
            if(review.content != null) reviewTextView.text = review.content
            if(review.authorDetails?.avatar != null && avatarImageView != null) {
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

class ReviewsDiffCallback: DiffUtil.ItemCallback<ReviewEntity>() {
    override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem.id != newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
        return oldItem != newItem
    }

}