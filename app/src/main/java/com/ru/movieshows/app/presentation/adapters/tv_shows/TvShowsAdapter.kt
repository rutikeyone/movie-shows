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
import com.ru.movieshows.databinding.TvShowsItemBinding
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

class TvShowsAdapter(
    private val tvShows: ArrayList<TvShowsEntity>,
    private val onTap: (TvShowsEntity) -> Unit,
) : RecyclerView.Adapter<TvShowsAdapter.TvShowHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tv_shows_item, parent, false)
        return TvShowHolder(view)
    }

    override fun getItemCount(): Int = tvShows.size

    override fun onBindViewHolder(holder: TvShowHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bind(tvShow, onTap)
    }

    class TvShowHolder(
        private val view: View,
    ) : RecyclerView.ViewHolder(view) {

        fun bind(
            tvShow: TvShowsEntity,
            onTap: (TvShowsEntity) -> Unit,
        ) {
            val binding = TvShowsItemBinding.bind(view).also {
                it.root.setOnClickListener { onTap(tvShow) }
            }
            bindLayoutParams(binding)
            bindTvShowName(tvShow, binding)
            bindTvShowImage(tvShow, binding)
            bindMovieRating(binding, tvShow.rating)
        }

        private fun bindTvShowImage(
            tvShow: TvShowsEntity,
            binding: TvShowsItemBinding
        ) = with(binding.tvShowsMovieImage) {
            val poster = tvShow.poster
            val context = this.context
            if(!poster.isNullOrEmpty()) {
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

        private fun bindTvShowName(
            tvShow: TvShowsEntity,
            binding: TvShowsItemBinding
        ) = with(binding.tvShowNameTextView) {
            val title = tvShow.name
            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindLayoutParams(binding: TvShowsItemBinding) = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_250),
            )
            this.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRating(
            binding: TvShowsItemBinding,
            rating: Double?,
        ) = with(binding) {
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
