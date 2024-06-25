package com.ru.movieshows.movies.presentation.adapters

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
import com.ru.movieshows.movies.databinding.SearchItemBinding
import com.ru.movieshows.movies.domain.entities.Movie

class MoviesSearchPaginationAdapter(
    private val listener: SimpleAdapterListener<Movie>,
) : PagingDataAdapter<Movie, MoviesSearchPaginationAdapter.Holder>(MovieDiffCalculator()),
    View.OnClickListener {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bindView(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val movie = view.tag as Movie
        listener.onClickItem(movie)
    }

    class Holder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(movie: Movie) {
            binding.root.tag = movie
            bindTitleView(movie)
            bindPosterView(movie)
            bindRatingView(movie)
            bindOverviewView(movie)
        }

        private fun bindTitleView(movie: Movie) {
            with(binding.headerText) {
                val title = movie.title
                if (!title.isNullOrEmpty()) {
                    text = movie.title
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        private fun bindOverviewView(movie: Movie) {
            with(binding.descriptionText) {
                val overview = movie.overview

                if (!overview.isNullOrEmpty()) {
                    text = overview
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        private fun bindRatingView(movie: Movie) {
            with(binding) {
                val rating = movie.rating

                ratingBar.isEnabled = false
                if (rating != null && rating > 0) {
                    val ratingText = "%.2f".format(rating)
                    val value = (rating.toFloat() / 2)
                    ratingValue.text = ratingText
                    ratingValue.isVisible = true
                    ratingBar.rating = value
                    ratingBar.isVisible = true
                    ratingBarContainer.isVisible = true
                } else {
                    ratingBar.isVisible = false
                    ratingValue.isVisible = false
                    ratingBarContainer.isVisible = false
                }
            }
        }

        private fun bindPosterView(movie: Movie) {
            with(binding.imageView) {
                val posterPath = movie.posterPath

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