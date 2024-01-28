package com.ru.movieshows.app.presentation.adapters.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.MovieTileViewPagerItemBinding
import com.ru.movieshows.sources.movies.entities.MovieEntity

class MoviesViewPagerAdapter(
    private val movies: ArrayList<MovieEntity>,
    private val listener: SimpleAdapterListener<MovieEntity>,
) : RecyclerView.Adapter<MoviesViewPagerAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieTileViewPagerItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = if(movies.size > 10) 10 else movies.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = movies[position]
        holder.bind(item)
    }

    override fun onClick(view: View) {
        val movie = view.tag as MovieEntity
        listener.onClickItem(movie)
    }

    inner class Holder(
        private val binding: MovieTileViewPagerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.root.tag = movie
            bindTitleUI(movie)
            bindMovieImageUI(movie)
        }

        private fun bindTitleUI(movie: MovieEntity) = with(binding.movieName) {
            val title = movie.title

            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
              isVisible = false
            }
        }

        private fun bindMovieImageUI(movie: MovieEntity) = with(binding.movieBackground) {
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

    }

}