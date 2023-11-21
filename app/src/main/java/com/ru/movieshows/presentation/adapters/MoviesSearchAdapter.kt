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
import com.ru.movieshows.presentation.adapters.diff_callback.NotEqualDiffItemCallback

class MoviesSearchAdapter(
    private val onTap: (MovieEntity) -> Unit,
) : PagingDataAdapter<MovieEntity, MoviesSearchAdapter.Holder>(NotEqualDiffItemCallback()) {

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

        private fun setupRating(movie: MovieEntity) = with(binding) {
            ratingBar.isEnabled = false
            if (movie.rating == null) return
            val rating = movie.rating

            if(rating > 0) {
                val ratingText = "%.2f".format(rating)
                val value = (rating.toFloat() / 2)
                ratingValue.text = ratingText
                ratingBar.rating = value
            } else {
                ratingBarContainer.isVisible = false
            }
        }

        private fun setupBackDrop(movie: MovieEntity) = with(binding.imageView) {
            val poster = movie.poster
            if(!poster.isNullOrEmpty()) {
                Glide
                    .with(binding.root)
                    .load(poster)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            } else {
                setImageResource(R.drawable.poster_placeholder_bg)
            }
        }

    }

}