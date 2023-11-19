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
import com.ru.movieshows.presentation.adapters.diff_callback.TvShowDiffCallback

class TvShowSearchAdapter(
    private val onTap:(TvShowsEntity) -> Unit,
) : PagingDataAdapter<TvShowsEntity, TvShowSearchAdapter.Holder>(TvShowDiffCallback()) {

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
            if(tvShow.backDrop != null) setupBackDrop(tvShow)
            setupRating(tvShow)
            setupDescription(tvShow)
        }

        private fun setupTitle(tvShow: TvShowsEntity) {
            if(tvShow.name == null) return
            binding.headerText.text = tvShow.name
        }

        private fun setupDescription(tvShow: TvShowsEntity) {
            if (tvShow.overview == null) return
            binding.descriptionText.text = tvShow.overview
        }

        private fun setupRating(tvShow: TvShowsEntity) {
            binding.ratingBar.isEnabled = false
            if (tvShow.rating == null) return
            val rating = tvShow.rating
            val ratingText = "%.2f".format(rating)
            val ratingValue = (rating.toFloat() / 2)
            binding.ratingValue.text = ratingText
            binding.ratingBar.rating = ratingValue
        }

        private fun setupBackDrop(tvShow: TvShowsEntity) {
            if(tvShow.backDrop == null) {
                binding.imageView.isVisible = false
                return
            }
            Glide
                .with(binding.root)
                .load(tvShow.backDrop)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        }

    }
}