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
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.tvshows.databinding.TvShowsItemBinding

class TvShowPaginationAdapter(
    private val listener: SimpleAdapterListener<TvShow>,
) : PagingDataAdapter<TvShow, TvShowPaginationAdapter.Holder>(TvShowDiffCalculator()),
    View.OnClickListener {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) TV_SHOW_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bind(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowsItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val tvShows = view.tag as TvShow
        listener.onClickItem(tvShows)
    }

    class Holder(
        private val binding: TvShowsItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShow) {
            binding.root.tag = tvShow
            bindTvShowName(tvShow)
            bindTvShowImage(tvShow)
            bindMovieRating(tvShow)
        }

        private fun bindTvShowImage(tvShow: TvShow) {
            with(binding.tvShowsMovieImageView) {
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

        private fun bindTvShowName(tvShow: TvShow) {
            with(binding.tvShowNameTextView) {
                val title = tvShow.name
                if (!title.isNullOrEmpty()) {
                    text = title
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRating(tvShow: TvShow) {
            val rating = tvShow.rating

            with(binding) {
                ratingBar.isEnabled = false

                if (rating != null) {
                    val ratingText = "%.2f".format(rating)
                    val value = (rating.toFloat() / 2)

                    ratingValueTextView.text = ratingText
                    ratingValueTextView.isVisible = true
                    ratingBar.rating = value
                    ratingBar.isVisible = true
                    ratingLinearLayout.isVisible = true

                } else {
                    ratingBar.isVisible = false
                    ratingValueTextView.isVisible = false
                    ratingLinearLayout.isVisible = false
                }
            }
        }
    }

    companion object {
        const val LOADING_ITEM = 0
        const val TV_SHOW_ITEM = 1
    }

}