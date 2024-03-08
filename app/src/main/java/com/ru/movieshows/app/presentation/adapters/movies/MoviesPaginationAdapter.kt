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
import com.ru.movieshows.app.databinding.MovieItemBinding
import com.ru.movieshows.sources.movies.entities.MovieEntity

class MoviesPaginationAdapter(
    private val listener: SimpleAdapterListener<MovieEntity>,
) : PagingDataAdapter<MovieEntity, MoviesPaginationAdapter.Holder>(MoviesDiffItemCallback()), View.OnClickListener {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) MOVIE_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val review = getItem(position) ?: return
        holder.bind(review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val item = view.tag as MovieEntity
        listener.onClickItem(item)
    }

    class Holder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.root.tag = movie
            bindMovieNameUI(movie, binding)
            bindMovieImageUI(movie, binding)
            bindMovieRatingUI(binding, movie.rating)
        }

        private fun bindMovieRatingUI(
            binding: MovieItemBinding,
            rating: Double?,
        ) = with(binding){
            ratingBar.isEnabled = false
            if(rating != null) {
                val ratingText = "%.2f".format(rating)
                val value = (rating.toFloat() / 2)
                ratingValue.text = ratingText
                ratingValue.isVisible = true
                ratingBar.rating = value
                ratingBar.isVisible = true
                ratingContainer.isVisible = true
            } else {
                ratingBar.isVisible = false
                ratingValue.isVisible = false
                ratingContainer.isVisible = false
            }
        }

        private fun bindMovieImageUI(
            movie: MovieEntity,
            binding: MovieItemBinding,
         ) = with(binding.discoverMovieImage) {
            val context = this.context
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

        private fun bindMovieNameUI(
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