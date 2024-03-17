package com.ru.movieshows.app.presentation.adapters.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.databinding.SearchItemBinding
import com.ru.movieshows.sources.movies.entities.MovieEntity

class MoviesSearchPaginationAdapter(
    private val listener: SimpleAdapterListener<MovieEntity>,
) : PagingDataAdapter<MovieEntity, MoviesSearchPaginationAdapter.Holder>(MoviesDiffItemCallback()), View.OnClickListener {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val movie = view.tag as MovieEntity
        listener.onClickItem(movie)
    }

    class Holder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.root.tag = movie
            bindTitleUI(movie)
            bindPosterUI(movie)
            bindRatingUI(movie)
            bindDescriptionUI(movie)
        }

        private fun bindTitleUI(movie: MovieEntity) = with(binding.headerText) {
            val title = movie.title
            if(!title.isNullOrEmpty()) {
                text = movie.title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindDescriptionUI(movie: MovieEntity) = with(binding.descriptionText) {
            val overview = movie.overview
            if(!overview.isNullOrEmpty()) {
                text = overview
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindRatingUI(movie: MovieEntity) = with(binding) {
            val rating = movie.rating
            ratingBar.isEnabled = false
            if(rating != null) {
                val ratingText = "%.2f".format(rating)
                val value = (rating.toFloat() / 2)
                ratingValue.text = ratingText
                ratingValue.isVisible = true
                ratingBar.rating = value
                ratingBar.isVisible = true
                ratingBarContainer.isVisible = true
            } else {
                ratingBar.isVisible = false
                ratingValue.isVisible = false
                ratingBarContainer.isVisible = false
            }
        }

        private fun bindPosterUI(movie: MovieEntity) = with(binding.imageView) {
            val poster = movie.poster
            val context = this.context
            if(!poster.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(poster)
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