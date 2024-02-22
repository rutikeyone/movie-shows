package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.TvShowsItemBinding
import com.ru.movieshows.sources.tvshows.entities.TvShowsEntity

class TvShowPaginationAdapter(
    private val listener: SimpleAdapterListener<TvShowsEntity>,
) : PagingDataAdapter<TvShowsEntity, TvShowPaginationAdapter.Holder>(TvShowDiffItemCallback()), View.OnClickListener {

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
        val tvShows = view.tag as TvShowsEntity
        listener.onClickItem(tvShows)
    }

    class Holder(
        private val binding: TvShowsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) {
            binding.root.tag = tvShow
            bindTvShowNameUI(tvShow)
            bindTvShowImageUI(tvShow)
            bindMovieRatingUI(tvShow.rating)
        }

        private fun bindTvShowImageUI(tvShow: TvShowsEntity) = with(binding.tvShowsMovieImage) {
            val poster = tvShow.poster
            if (!poster.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(poster)
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

        private fun bindTvShowNameUI(tvShow: TvShowsEntity) = with(binding.tvShowNameTextView) {
            val title = tvShow.name
            if (!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRatingUI(rating: Double?) = with(binding) {
            ratingBar.isEnabled = false
            if (rating != null) {
                val ratingText = "%.2f".format(rating)
                val value = (rating.toFloat() / 2)
                ratingValue.text = ratingText
                ratingValue.isVisible = true
                ratingBar.rating = value
                ratingBar.isVisible = true
                ratingContainer.isVisible = true
            } else {
                ratingBar.isVisible = false
                ratingValue.isVisible = false
                ratingContainer.isVisible = false
            }
        }
    }

    companion object {
        const val LOADING_ITEM = 0
        const val TV_SHOW_ITEM = 1
    }

}