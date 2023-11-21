package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.MovieTileVariant1Binding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.adapters.diff_callback.MoviesDiffItemCallback

class MoviesListAdapter(
    private val onTap: (MovieEntity) -> Unit,
) : PagingDataAdapter<MovieEntity, MoviesListAdapter.Holder>(MoviesDiffItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) MOVIE_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = getItem(position) ?: return
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieTileVariant1Binding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    class Holder(
        private val binding: MovieTileVariant1Binding,
        private val onTap: (MovieEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            setupMovieName(movie, binding)
            setupMovieImage(movie, binding)
            setupMovieRating(binding, movie.rating)
            binding.root.setOnClickListener { onTap(movie) }
        }

        private fun setupMovieRating(
            binding: MovieTileVariant1Binding,
            rating: Double?,
        ) = with(binding){
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                ratingValue.text = value.toString()
                ratingBar.isEnabled = false;
            } else {
                ratingValue.visibility = View.GONE
                ratingBar.visibility = View.GONE
            }
        }

        private fun setupMovieImage(
            movie: MovieEntity,
            binding: MovieTileVariant1Binding,
         ) = with(binding.discoverMovieImage) {
            val backDrop = movie.backDrop
            if(!backDrop.isNullOrEmpty()) {
                Glide
                    .with(binding.root)
                    .load(backDrop)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            } else {
                setImageResource(R.drawable.poster_placeholder_bg)
            }
        }

        private fun setupMovieName(
            movie: MovieEntity,
            binding: MovieTileVariant1Binding,
        ) = with(binding.movieName) {
            val title = movie.title
            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

    }

    companion object {
        const val LOADING_ITEM = 0
        const val MOVIE_ITEM = 1
    }
}