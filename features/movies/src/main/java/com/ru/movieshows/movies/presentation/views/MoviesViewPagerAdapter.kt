package com.ru.movieshows.movies.presentation.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.movies.databinding.MovieViewPagerItemBinding
import com.ru.movieshows.movies.domain.entities.Movie

class MoviesViewPagerAdapter(
    private val movies: List<Movie>,
    private val listener: SimpleAdapterListener<Movie>,
) : ListAdapter<Movie, MoviesViewPagerAdapter.Holder>(MovieDiffCalculator()), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieViewPagerItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = if (movies.size > 10) 10 else movies.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = movies[position]
        holder.bind(item)
    }

    override fun onClick(view: View) {
        val movie = view.tag as Movie
        listener.onClickItem(movie)
    }

    inner class Holder(
        private val binding: MovieViewPagerItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.root.tag = movie
            bindTitle(movie)
            bindMovieImage(movie)
        }

        private fun bindTitle(movie: Movie) {
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

        private fun bindMovieImage(movie: Movie) {
            with(binding.movieBackgroundImageView) {
                val backDrop = movie.backDropPath
                val loadBackDrop =
                    if (!backDrop.isNullOrEmpty()) backDrop
                    else R.drawable.core_presentation_bg_poster_placeholder

                Glide
                    .with(context)
                    .load(loadBackDrop)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }

}