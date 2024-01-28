package com.ru.movieshows.app.presentation.adapters.reviews

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.utils.OnTouchListener
import com.ru.movieshows.databinding.ReviewItemBinding
import com.ru.movieshows.sources.movies.entities.ReviewEntity

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

        fun bind(review: ReviewEntity) {
            if (review.author != null) binding.reviewerHeaderView.text = review.author
            bindRatingUI(review)
            bindAvatarImageViewUI(review)
            bindContentUI(review)
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun bindContentUI(review: ReviewEntity) = with(binding) {
            val content = review.content
            if (!content.isNullOrEmpty()) {
                val value = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                val onTouchListener = OnTouchListener()
                reviewTextView.text = value
                reviewTextView.isVisible = true
                reviewTextView.setOnTouchListener(onTouchListener)
            } else {
                reviewTextView.isVisible = false
            }
        }

        private fun bindRatingUI(review: ReviewEntity) = with(binding.ratingBar) {
            val rating = review.authorDetails?.rating
            if (rating != null && rating > 2) {
                val value = review.authorDetails.rating.toFloat() / 2
                this.rating = value
                this.isVisible = true
            } else {
                this.isVisible = false
            }
        }

        private fun bindAvatarImageViewUI(review: ReviewEntity) = with(binding.avatarImageView) {
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
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .centerCrop()
                    .into(this)
            }
        }

    }

}