package com.ru.movieshows.presentation.adapters

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.TvShowsTileVariant1Binding
import com.ru.movieshows.domain.entity.TvShowsEntity

class TvShowsAdapter(
    private val tvShows: ArrayList<TvShowsEntity>,
    private val onTap: (TvShowsEntity) -> Unit,
) : RecyclerView.Adapter<TvShowsAdapter.TvShowHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tv_shows_tile_variant1, parent, false)
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
            val binding = TvShowsTileVariant1Binding.bind(view)
            binding.root.setOnClickListener { onTap(tvShow) }
            setupLayoutParams(binding)
            setupTvShowName(tvShow, binding)
            setupTvShowImage(tvShow, binding)
            setupMovieRating(binding, tvShow.rating)
        }

        private fun setupTvShowImage(
            tvShow: TvShowsEntity,
            binding: TvShowsTileVariant1Binding
        ) {
            val poster = tvShow.poster ?: return
            Glide
                .with(binding.root)
                .load(poster)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tvShowsMovieImage)
        }

        private fun setupTvShowName(
            tvShow: TvShowsEntity,
            binding: TvShowsTileVariant1Binding
        ) = with(binding) {
            val title = tvShow.name ?: return
            movieName.text = title
        }

        private fun setupLayoutParams(binding: TvShowsTileVariant1Binding) {
            val layoutParams = ActionBar.LayoutParams(
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.root.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun setupMovieRating(
            binding: TvShowsTileVariant1Binding,
            rating: Double?,
        ) {
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                binding.ratingValue.text = "%.2f".format(value)
                binding.ratingBar.isEnabled = false;
            } else {
                binding.ratingValue.visibility = View.GONE
                binding.ratingBar.visibility = View.GONE
            }
        }

    }

}
