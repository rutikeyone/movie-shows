package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.MovieTileViewPagerItemBinding
import com.ru.movieshows.domain.entity.MovieEntity

class MoviesViewPagerAdapter(
    private val movies: ArrayList<MovieEntity>,
    private val onTap: (MovieEntity) -> Unit,
    ) : RecyclerView.Adapter<MoviesViewPagerAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.movie_tile_view_pager_item, parent, false)
        return Holder(item, onTap)
    }

    override fun getItemCount(): Int = if(movies.size > 10) 10 else movies.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = movies[position]
        holder.bind(item)
    }

    inner class Holder(
        private val view: View,
        private val onTap: (MovieEntity) -> Unit,
        ) : RecyclerView.ViewHolder(view) {

        fun bind(movie: MovieEntity) {
            val binding = MovieTileViewPagerItemBinding.bind(view).also {
                it.root.setOnClickListener {
                    onTap(movie)
                }
            }
            bindTitle(binding, movie)
            bindMovieImage(binding, movie)
        }

        private fun bindTitle(
            binding: MovieTileViewPagerItemBinding,
            movie: MovieEntity
        ) = with(binding.movieName) {
            val title = movie.title

            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
              isVisible = false
            }
        }

        private fun bindMovieImage(
            binding: MovieTileViewPagerItemBinding,
            movie: MovieEntity
        ) = with(binding.movieBackground) {
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

    }
}