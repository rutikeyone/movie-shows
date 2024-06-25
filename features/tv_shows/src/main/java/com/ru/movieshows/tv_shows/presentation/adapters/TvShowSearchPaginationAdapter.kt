package com.ru.movieshows.tv_shows.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tvshows.databinding.SearchItemBinding

class TvShowSearchPaginationAdapter(
    private val listener: SimpleAdapterListener<TvShow>,
) : PagingDataAdapter<TvShow, TvShowSearchPaginationAdapter.Holder>(TvShowDiffCalculator()),
    View.OnClickListener {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bindView(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val item = view.tag as TvShow
        listener.onClickItem(item)
    }

    class Holder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(tvShow: TvShow) {
            binding.root.tag = tvShow
            bindTitleView(tvShow)
            setupPosterView(tvShow)
            bindRatingView(tvShow)
            bindDescriptionView(tvShow)
        }

        private fun bindTitleView(tvShow: TvShow) {
            with(binding.headerText) {
                val name = tvShow.name
                if (name != null) {
                    text = name
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        private fun bindDescriptionView(tvShow: TvShow) {
            with(binding) {
                val overview = tvShow.overview

                if (overview != null) {
                    descriptionText.text = tvShow.overview
                    descriptionText.isVisible = true
                } else {
                    descriptionText.isVisible = false
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindRatingView(tvShow: TvShow) {
            with(binding) {
                val rating = tvShow.rating

                ratingBar.isEnabled = false
                if (rating != null && rating > 0) {
                    val value = rating.toFloat() / 2
                    ratingValue.text = "%.2f".format(rating)
                    ratingBar.rating = value
                    ratingValue.isVisible = true
                    ratingBar.isVisible = true
                    ratingBarContainer.isVisible = true
                } else {
                    ratingValue.isVisible = false
                    ratingBar.isVisible = false
                    ratingBarContainer.isVisible = false
                }
            }
        }

        private fun setupPosterView(tvShow: TvShow) {
            with(binding.imageView) {
                val posterPath = tvShow.posterPath

                Glide
                    .with(context)
                    .load(posterPath)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }

}