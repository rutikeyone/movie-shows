package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val binding = MovieTileViewPagerItemBinding.bind(view)
            setupTitle(binding, movie)
            setupMovieImage(binding, movie)
            binding.root.setOnClickListener { onTap(movie) }
        }

        private fun setupTitle(
            binding: MovieTileViewPagerItemBinding,
            movie: MovieEntity
        ) {
            val title = movie.title ?: return
            binding.movieName.text = title
        }

        private fun setupMovieImage(
            binding: MovieTileViewPagerItemBinding,
            movie: MovieEntity
        ) {
            val backDrop = movie.backDrop ?: return
            Glide
                .with(binding.root)
                .load(backDrop)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.movieBackground)
        }

    }
}