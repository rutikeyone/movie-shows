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
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.adapters.diff_callback.NotEqualDiffItemCallback

class TvShowSearchPaginationAdapter(
    private val onTap:(TvShowsEntity) -> Unit,
) : PagingDataAdapter<TvShowsEntity, TvShowSearchPaginationAdapter.Holder>(NotEqualDiffItemCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bind(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    class Holder(
        private val binding: SearchItemBinding,
        private val onTap: (TvShowsEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) = with(binding.root) {
            setOnClickListener { onTap(tvShow) }
            bindTitle(tvShow)
            setupBackDrop(tvShow)
            bindRating(tvShow)
            bindDescription(tvShow)
        }

        private fun bindTitle(tvShow: TvShowsEntity) = with(binding.headerText) {
            val name = tvShow.name
            if(name != null) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindDescription(tvShow: TvShowsEntity) = with(binding) {
            val overview = tvShow.overview
            if(overview != null) {
                descriptionText.text = tvShow.overview
                descriptionText.isVisible = true
            } else {
                descriptionText.isVisible = false
            }

        }

        private fun bindRating(tvShow: TvShowsEntity) = with(binding) {
            val rating = tvShow.rating
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat() / 2
                ratingValue.text = "%.2f".format(value)
                ratingBar.rating = value
                ratingValue.isVisible = true
                ratingBar.isVisible = true
                ratingBarContainer.isVisible = true
            } else {
                ratingValue.isVisible = false
                ratingBar.isVisible = false
                ratingBarContainer.isVisible = false
            }
        }

        private fun setupBackDrop(tvShow: TvShowsEntity) = with(binding.imageView) {
            val poster = tvShow.poster
            if(!poster.isNullOrEmpty()) {
                Glide
                    .with(binding.root)
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