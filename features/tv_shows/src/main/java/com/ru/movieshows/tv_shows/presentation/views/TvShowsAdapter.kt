package com.ru.movieshows.tv_shows.presentation.views

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.TvShowsItemBinding

class TvShowsAdapter(
    private val tvShows: List<TvShow>,
    private val listener: SimpleAdapterListener<TvShow>,
) : ListAdapter<TvShow, TvShowsAdapter.TvShowHolder>(TvShowDiffCalculator()), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowsItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return TvShowHolder(binding)
    }

    override fun getItemCount(): Int = tvShows.size

    override fun onBindViewHolder(holder: TvShowHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bindViews(tvShow)
    }

    override fun onClick(view: View) {
        val item = view.tag as TvShow
        listener.onClickItem(item)
    }

    class TvShowHolder(
        private val binding: TvShowsItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(tvShow: TvShow) {
            binding.root.tag = tvShow
            bindLayoutParamsView()
            bindTvShowNameView(tvShow)
            bindTvShowImageView(tvShow)
            bindMovieRatingView(tvShow.rating)
        }

        private fun bindTvShowImageView(tvShow: TvShow) = with(binding.tvShowsMovieImageView) {
            val context = this.context

            val poster = tvShow.posterPath
            val loadBackDrop =
                if (!poster.isNullOrEmpty()) poster
                else R.drawable.bg_poster_placeholder

            Glide
                .with(context)
                .load(loadBackDrop)
                .centerCrop()
                .placeholder(R.drawable.bg_poster_placeholder)
                .error(R.drawable.bg_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }

        private fun bindTvShowNameView(tvShow: TvShow) = with(binding.tvShowNameTextView) {
            val title = tvShow.name
            if (!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindLayoutParamsView() = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_250),
            )
            this.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRatingView(rating: Double?) = with(binding) {
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                ratingValueTextView.text = "%.2f".format(value)
                ratingValueTextView.isVisible = true
                ratingBar.isVisible = true
                ratingLinearLayout.isVisible = true
            } else {
                ratingValueTextView.isVisible = false
                ratingBar.isVisible = false
                ratingLinearLayout.isVisible = false
            }
        }

    }

}
