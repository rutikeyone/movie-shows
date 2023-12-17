package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SearchItemBinding
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.adapters.diff_callback.NotEqualDiffItemCallback

class MoviesSearchPaginationAdapter(
    private val onTap: (MovieEntity) -> Unit,
) : PagingDataAdapter<MovieEntity, MoviesSearchPaginationAdapter.Holder>(NotEqualDiffItemCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = getItem(position) ?: return
        holder.bind(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }


class Holder(
    private val binding: SearchItemBinding,
    private val onTap: (MovieEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) = with(binding) {
            binding.root.setOnClickListener { onTap(movie) }
            bindTitle(movie)
            bindPoster(movie)
            bindRating(movie)
            bindDescription(movie)
        }

        private fun bindTitle(movie: MovieEntity) = with(binding.headerText){
            val title = movie.title
            if(!title.isNullOrEmpty()) {
                text = movie.title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindDescription(movie: MovieEntity) = with(binding.descriptionText) {
            val overview = movie.overview
            if(!overview.isNullOrEmpty()) {
                text = overview
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindRating(movie: MovieEntity) = with(binding) {
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

        private fun bindPoster(movie: MovieEntity) = with(binding.imageView) {
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
                    .centerCrop()
                    .into(this)
            }
        }

    }

}