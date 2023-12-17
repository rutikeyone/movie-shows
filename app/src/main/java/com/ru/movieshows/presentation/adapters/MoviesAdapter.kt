package com.ru.movieshows.presentation.adapters

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
import com.ru.movieshows.databinding.MovieItemBinding
import com.ru.movieshows.domain.entity.MovieEntity

class MoviesAdapter(
    private val onTap: (MovieEntity) -> Unit,
): RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {
    private var movies = arrayListOf<MovieEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.movie_item, parent, false)
        return MoviesHolder(item, onTap)
    }

    override fun getItemViewType(position: Int): Int = if (position == itemCount) MOVIE_ITEM else LOADING_ITEM

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: ArrayList<MovieEntity>) {
        this.movies = data
        notifyDataSetChanged()
    }

    class MoviesHolder(
        private val view: View,
        private val onTap: (MovieEntity) -> Unit,
    ) : RecyclerView.ViewHolder(view) {

        fun bind(movie: MovieEntity) {
            val binding = MovieItemBinding.bind(view)
            binding.root.setOnClickListener { onTap(movie) }
            bindLayoutParams(binding)
            bindMovieName(movie, binding)
            bindMovieImage(movie, binding)
            bindMovieRating(binding, movie.rating)
        }

        private fun bindLayoutParams(binding: MovieItemBinding) = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_250),
            )
            this.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRating(
            binding: MovieItemBinding,
            rating: Double?,
        ) = with(binding) {
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat() / 2
                ratingValue.text = "%.2f".format(rating)
                ratingBar.rating = value
                ratingValue.isVisible = true
                ratingBar.isVisible = true
                ratingContainer.isVisible = true
            } else {
                ratingValue.isVisible = false
                ratingBar.isVisible = false
                ratingContainer.isVisible = false
            }
        }

        private fun bindMovieImage(
            movie: MovieEntity,
            binding: MovieItemBinding,
        ) = with(binding.discoverMovieImage){
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
