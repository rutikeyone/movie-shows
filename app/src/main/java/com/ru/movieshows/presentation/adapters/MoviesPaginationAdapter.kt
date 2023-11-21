package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.MovieItemBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.adapters.diff_callback.MoviesDiffItemCallback

class MoviesPaginationAdapter(
    private val onTap: (MovieEntity) -> Unit,
) : PagingDataAdapter<MovieEntity, MoviesPaginationAdapter.Holder>(MoviesDiffItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) MOVIE_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = getItem(position) ?: return
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    class Holder(
        private val binding: MovieItemBinding,
        private val onTap: (MovieEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.root.setOnClickListener { onTap(movie) }
            bindMovieName(movie, binding)
            bindMovieImage(movie, binding)
            bindMovieRating(binding, movie.rating)
        }

        private fun bindMovieRating(
            binding: MovieItemBinding,
            rating: Double?,
        ) = with(binding){
            ratingBar.isEnabled = false
            if(rating != null) {
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

        private fun bindMovieImage(
            movie: MovieEntity,
            binding: MovieItemBinding,
         ) = with(binding.discoverMovieImage) {
            val context = this.context
            val backDrop = movie.backDrop
            if(!backDrop.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(backDrop)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            } else {
                Glide
                    .with(context)
                    .load(R.drawable.poster_placeholder_bg)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

        private fun bindMovieName(
            movie: MovieEntity,
            binding: MovieItemBinding,
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