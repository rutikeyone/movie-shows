package com.ru.movieshows.movies.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.movies.databinding.MovieItemBinding
import com.ru.movieshows.movies.domain.entities.Movie

class MoviesPaginationAdapter(
    private val listener: SimpleAdapterListener<Movie>,
) : PagingDataAdapter<Movie, MoviesPaginationAdapter.Holder>(MovieDiffCalculator()),
    View.OnClickListener {

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
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val item = view.tag as Movie
        listener.onClickItem(item)
    }

    class Holder(
        private val binding: MovieItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.root.tag = movie
            bindMovieName(movie)
            bindMovieImage(movie)
            bindMovieRating(movie)
        }

        private fun bindMovieRating(movie: Movie) {
            val rating = movie.rating

            with(binding) {
                ratingBar.isEnabled = false

                if (rating != null) {
                    val ratingText = "%.2f".format(rating)
                    val value = (rating.toFloat() / 2)

                    ratingValueTextView.text = ratingText
                    ratingValueTextView.isVisible = true
                    ratingBar.rating = value
                    ratingBar.isVisible = true
                    ratingLinearLayout.isVisible = true

                } else {
                    ratingBar.isVisible = false
                    ratingValueTextView.isVisible = false
                    ratingLinearLayout.isVisible = false
                }
            }
        }

        private fun bindMovieImage(movie: Movie) {
            with(binding.discoverMovieImageView) {
                val backDropPath = movie.backDropPath

                Glide
                    .with(context)
                    .load(backDropPath)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

        private fun bindMovieName(movie: Movie) {
            with(binding.movieNameTextView) {
                val title = movie.title
                if (!title.isNullOrEmpty()) {
                    text = title
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }
    }

    companion object {
        const val LOADING_ITEM = 0
        const val MOVIE_ITEM = 1
    }

}