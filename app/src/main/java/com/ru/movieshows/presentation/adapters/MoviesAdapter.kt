package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.MovieTileVariant1Binding
import com.ru.movieshows.domain.entity.MovieEntity

class MoviesAdapter(
    private val movies: ArrayList<MovieEntity>,
    private val onTap: (MovieEntity) -> Unit,
): RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val tile = inflater.inflate(R.layout.movie_tile_variant1, parent, false)
        return MoviesHolder(tile, onTap)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        holder.bind(movies[position])
    }

    class MoviesHolder(private val view: View, private val onTap: (MovieEntity) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(movie: MovieEntity) {
            val binding = MovieTileVariant1Binding.bind(view)
            setupMovieName(movie, binding)
            setupMovieImage(movie, binding)
            setupMovieRating(binding, movie.rating)
            binding.root.setOnClickListener { onTap(movie) }
        }

        private fun setupMovieRating(
            binding: MovieTileVariant1Binding,
            rating: Double?,
        ) {
            if (rating != null) {
                val value = rating.toFloat()
                binding.ratingValue.text = value.toString()
                binding.ratingBar.isEnabled = false;
            } else {
                binding.ratingValue.visibility = View.GONE
                binding.ratingBar.visibility = View.GONE
            }
        }

        private fun setupMovieImage(
            movie: MovieEntity,
            binding: MovieTileVariant1Binding,
        ) {
            if (movie.backDrop != null) {
                Glide
                    .with(binding.root)
                    .load(movie.backDrop)
                    .centerCrop()
                    .into(binding.discoverMovieImage);
            }
        }

        private fun setupMovieName(
            movie: MovieEntity,
            binding: MovieTileVariant1Binding,
        ) {
            if (movie.title != null) binding.movieName.text = movie.title
        }
    }
}
