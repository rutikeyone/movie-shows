package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.databinding.MovieSearchTileBinding
import com.ru.movieshows.domain.entity.MovieEntity

class MoviesSearchAdapter(
    private val onTap: (MovieEntity) -> Unit,
) : PagingDataAdapter<MovieEntity, MoviesSearchAdapter.Holder>(MoviesDiffCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieSearchTileBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }


class Holder(
    private val binding: MovieSearchTileBinding,
    private val onTap: (MovieEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) = with(binding) {
            binding.root.setOnClickListener { onTap(movie) }
            setupTitle(movie)
            if(movie.backDrop != null) setupBackDrop(movie)
            setupRating(movie)
            setupDescription(movie)
        }

        private fun setupTitle(movie: MovieEntity) {
            if(movie.title == null) return
            binding.movieHeaderText.text = movie.title
        }

        private fun setupDescription(movie: MovieEntity) {
            if (movie.overview == null) return
            binding.descriptionText.text = movie.overview
        }

        private fun setupRating(movie: MovieEntity) {
            binding.ratingBar.isEnabled = false
            if (movie.rating == null) return
            val rating = movie.rating
            val ratingText = "%.2f".format(rating)
            val ratingValue = (rating.toFloat() / 2)
            binding.ratingValue.text = ratingText
            binding.ratingBar.rating = ratingValue
        }

        private fun setupBackDrop(movie: MovieEntity) {
            if(movie.backDrop == null) {
                binding.discoverMovieImage.isVisible = false
                return
            }
            Glide
                .with(binding.root)
                .load(movie.backDrop)
                .centerCrop()
                .into(binding.discoverMovieImage)
        }

    }

}