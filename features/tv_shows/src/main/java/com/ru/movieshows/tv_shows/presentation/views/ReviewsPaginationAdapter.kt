package com.ru.movieshows.tv_shows.presentation.views

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
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.tvshows.databinding.TvShowReviewItemBinding

class ReviewsPaginationAdapter :
    PagingDataAdapter<Review, ReviewsPaginationAdapter.Holder>(ReviewsDiffItemCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = getItem(position) ?: return
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowReviewItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        private val binding: TvShowReviewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            bindAuthor(review)
            bindRating(review)
            bindAvatarImageView(review)
            bindContent(review)
        }

        private fun bindAuthor(review: Review) {
            val author = review.author

            with(binding.reviewerHeaderTextView) {
                if (!author.isNullOrEmpty()) {
                    text = author
                    isVisible = true
                } else {
                    isVisible = false
                }
            }

        }

        @SuppressLint("ClickableViewAccessibility")
        private fun bindContent(review: Review) {
            with(binding) {
                val content = review.content

                if (!content.isNullOrEmpty()) {
                    val value = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)

                    with(reviewTextView) {
                        text = value
                        isVisible = true
                        setOnClickListener {
                            toggle()
                        }
                    }

                } else {
                    reviewTextView.isVisible = false
                }
            }
        }

        private fun bindRating(review: Review) {
            with(binding.ratingBar) {
                val rating = review.authorDetails?.rating
                if (rating != null && rating > 2) {
                    val authorRating = review.authorDetails.rating.toFloat()
                    val value = authorRating / 2
                    this.rating = value
                    this.isVisible = true
                } else {
                    this.isVisible = false
                }
            }
        }

        private fun bindAvatarImageView(review: Review) {
            with(binding.avatarImageView) {
                val authorDetails = review.authorDetails
                val loadImage = authorDetails?.avatarPath

                Glide
                    .with(context)
                    .load(loadImage)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }

}