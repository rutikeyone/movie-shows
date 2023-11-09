package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SearchTileBinding
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
        val binding = SearchTileBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }


class Holder(
    private val binding: SearchTileBinding,
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
            binding.headerText.text = movie.title
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
                binding.imageView.isVisible = false
                return
            }
            Glide
                .with(binding.root)
                .load(movie.backDrop)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        }

    }

}