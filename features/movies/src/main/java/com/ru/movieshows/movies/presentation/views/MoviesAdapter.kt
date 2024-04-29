package com.ru.movieshows.movies.presentation.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.MovieItemBinding
import com.ru.movieshows.movies.domain.entities.Movie

class MoviesAdapter(
    private val listener: SimpleAdapterListener<Movie>,
) : RecyclerView.Adapter<MoviesAdapter.MoviesHolder>(), View.OnClickListener {

    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return MoviesHolder(binding)
    }

    override fun getItemViewType(position: Int): Int =
        if (position == itemCount) MOVIE_ITEM else LOADING_ITEM

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Movie>) {
        this.movies = data
        notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        val movie = v.tag as Movie
        listener.onClickItem(movie)
    }

    class MoviesHolder(
        private val binding: MovieItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.root.tag = movie
            bindLayoutParams(binding)
            bindMovieName(movie)
            bindMovieImage(movie)
            bindMovieRating(movie.rating)
        }

        private fun bindLayoutParams(binding: MovieItemBinding) = with(binding.root) {
            val layoutParams = android.app.ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_255),
            )
            this.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRating(rating: Double?) = with(binding) {
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat() / 2
                ratingValueTextView.text = "%.2f".format(rating)
                ratingBar.rating = value
                ratingValueTextView.isVisible = true
                ratingBar.isVisible = true
                ratingLinearLayout.isVisible = true
            } else {
                ratingValueTextView.isVisible = false
                ratingBar.isVisible = false
                ratingLinearLayout.isVisible = false
            }
        }

        private fun bindMovieImage(movie: Movie) {
            with(binding.discoverMovieImageView) {
                val backDrop = movie.backDropPath
                val loadBackDrop =
                    if (!backDrop.isNullOrEmpty()) backDrop
                    else R.drawable.bg_poster_placeholder

                Glide
                    .with(context)
                    .load(loadBackDrop)
                    .centerCrop()
                    .placeholder(R.drawable.bg_poster_placeholder)
                    .error(R.drawable.bg_poster_placeholder)
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