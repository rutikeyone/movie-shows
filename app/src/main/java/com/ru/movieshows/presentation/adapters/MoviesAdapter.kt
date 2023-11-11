package com.ru.movieshows.presentation.adapters

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.MovieTileVariant1Binding
import com.ru.movieshows.domain.entity.MovieEntity

class MoviesAdapter(
    private val onTap: (MovieEntity) -> Unit,
): RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {
    private var movies = arrayListOf<MovieEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.movie_tile_variant1, parent, false)
        return MoviesHolder(item, onTap)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) MOVIE_ITEM else LOADING_ITEM
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateData(data: ArrayList<MovieEntity>) {
        this.movies.clear()
        this.movies.addAll(data)
        notifyDataSetChanged()
    }

    class MoviesHolder(
        private val view: View,
        private val onTap: (MovieEntity) -> Unit,
    ) : RecyclerView.ViewHolder(view) {

        fun bind(movie: MovieEntity) {
            val binding = MovieTileVariant1Binding.bind(view)
            setupLayoutParams(binding)
            setupMovieName(movie, binding)
            setupMovieImage(movie, binding)
            setupMovieRating(binding, movie.rating)
            binding.root.setOnClickListener { onTap(movie) }
        }

        private fun setupLayoutParams(binding: MovieTileVariant1Binding) {
            val layoutParams = ActionBar.LayoutParams(
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.root.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun setupMovieRating(
            binding: MovieTileVariant1Binding,
            rating: Double?,
        ) {
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                binding.ratingValue.text = "%.2f".format(value)
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
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.discoverMovieImage)
            }
        }

        private fun setupMovieName(
            movie: MovieEntity,
            binding: MovieTileVariant1Binding,
        ) {
            if (movie.title != null) binding.movieName.text = movie.title
        }
    }

    companion object {
        const val LOADING_ITEM = 0
        const val MOVIE_ITEM = 1
    }
}
