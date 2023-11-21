package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SearchTileBinding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.adapters.diff_callback.NotEqualDiffItemCallback

class TvShowSearchAdapter(
    private val onTap:(TvShowsEntity) -> Unit,
) : PagingDataAdapter<TvShowsEntity, TvShowSearchAdapter.Holder>(NotEqualDiffItemCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bind(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchTileBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    class Holder(
        private val binding: SearchTileBinding,
        private val onTap: (TvShowsEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) = with(binding) {
            binding.root.setOnClickListener { onTap(tvShow) }
            setupTitle(tvShow)
            setupBackDrop(tvShow)
            setupRating(tvShow)
            setupDescription(tvShow)
        }

        private fun setupTitle(tvShow: TvShowsEntity) = with(binding.headerText) {
            val name = tvShow.name
            if(name != null) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun setupDescription(tvShow: TvShowsEntity) = with(binding) {
            val overview = tvShow.overview
            if(overview != null) {
                descriptionText.text = tvShow.overview
                descriptionText.isVisible = true
            } else {
                descriptionText.isVisible = false
            }

        }

        private fun setupRating(tvShow: TvShowsEntity) {
            val rating = tvShow.rating
            binding.ratingBar.isEnabled = false
            if(rating != null) {
                val ratingText = "%.2f".format(rating)
                val ratingValue = (rating.toFloat() / 2)
                binding.ratingValue.text = ratingText
                binding.ratingBar.rating = ratingValue
                binding.ratingBarContainer.isVisible = true
            } else {
                binding.ratingBarContainer.isVisible = false
            }
        }

        private fun setupBackDrop(tvShow: TvShowsEntity) = with(binding.imageView) {
            val poster = tvShow.poster
            if (!poster.isNullOrEmpty()) {
                Glide
                    .with(binding.root)
                    .load(poster)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            } else {
                setImageResource(R.drawable.poster_placeholder_bg)
            }
        }
    }
}