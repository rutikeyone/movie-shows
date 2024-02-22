package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.TvShowsItemBinding
import com.ru.movieshows.sources.tvshows.entities.TvShowsEntity

class TvShowsAdapter(
    private val tvShows: ArrayList<TvShowsEntity>,
    private val listener: SimpleAdapterListener<TvShowsEntity>,
) : RecyclerView.Adapter<TvShowsAdapter.TvShowHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowsItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return TvShowHolder(binding)
    }

    override fun getItemCount(): Int = tvShows.size

    override fun onBindViewHolder(holder: TvShowHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bind(tvShow)
    }

    override fun onClick(view: View) {
        val item = view.tag as TvShowsEntity
        listener.onClickItem(item)
    }

    class TvShowHolder(
        private val binding: TvShowsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) {
            binding.root.tag = tvShow
            bindLayoutParamsUI()
            bindTvShowNameUI(tvShow)
            bindTvShowImageUI(tvShow)
            bindMovieRatingUI(tvShow.rating)
        }

        private fun bindTvShowImageUI(tvShow: TvShowsEntity) = with(binding.tvShowsMovieImage) {
            val poster = tvShow.poster
            val context = this.context
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

        private fun bindLayoutParamsUI() = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_250),
            )
            this.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRatingUI(rating: Double?) = with(binding) {
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                ratingValue.text = "%.2f".format(value)
                ratingValue.isVisible = true
                ratingBar.isVisible = true
                ratingContainer.isVisible = true
            } else {
                ratingValue.isVisible = false
                ratingBar.isVisible = false
                ratingContainer.isVisible = false
            }
        }

    }

}
