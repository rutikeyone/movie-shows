package com.ru.movieshows.app.presentation.adapters.movies

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.databinding.MovieItemBinding
import com.ru.movieshows.sources.movies.entities.MovieEntity

class MoviesAdapter(
    private val listener: SimpleAdapterListener<MovieEntity>,
): RecyclerView.Adapter<MoviesAdapter.MoviesHolder>(), View.OnClickListener {

    private var movies = arrayListOf<MovieEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return MoviesHolder(binding)
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

    override fun onClick(v: View) {
        val movie = v.tag as MovieEntity
        listener.onClickItem(movie)
    }

    class MoviesHolder(
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.root.tag = movie
            bindLayoutParamsUI(binding)
            bindMovieNameUI(movie)
            bindMovieImageUI(movie)
            bindMovieRatingUI(movie.rating)
        }

        private fun bindLayoutParamsUI(binding: MovieItemBinding) = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_255),
            )
            this.layoutParams = layoutParams
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRatingUI(rating: Double?) = with(binding) {
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

        private fun bindMovieImageUI(movie: MovieEntity) = with(binding.discoverMovieImage){
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
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .centerCrop()
                    .into(this)
            }
        }

        private fun bindMovieNameUI(movie: MovieEntity) = with(binding.movieName) {
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
