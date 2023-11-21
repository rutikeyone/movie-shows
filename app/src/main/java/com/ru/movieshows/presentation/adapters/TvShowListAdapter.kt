package com.ru.movieshows.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.TvShowsTileVariant1Binding
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.adapters.diff_callback.TvShowDiffItemCallback

class TvShowListAdapter(
    private val onTap: (TvShowsEntity) -> Unit,
) : PagingDataAdapter<TvShowsEntity, TvShowListAdapter.Holder>(TvShowDiffItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) TV_SHOW_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bind(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowsTileVariant1Binding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    class Holder(
        private val binding: TvShowsTileVariant1Binding,
        private val onTap: (TvShowsEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            tvShow: TvShowsEntity,
        ) {
            binding.root.setOnClickListener { onTap(tvShow) }
            setupTvShowName(tvShow, binding)
            setupTvShowImage(tvShow, binding)
            setupMovieRating(binding, tvShow.rating)
        }

        private fun setupTvShowImage(
            tvShow: TvShowsEntity,
            binding: TvShowsTileVariant1Binding
        ) {
            val poster = tvShow.poster ?: return
            Glide
                .with(binding.root)
                .load(poster)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tvShowsMovieImage)
        }

        private fun setupTvShowName(
            tvShow: TvShowsEntity,
            binding: TvShowsTileVariant1Binding
        ) = with(binding) {
            val title = tvShow.name ?: return
            movieName.text = title
        }

        @SuppressLint("SetTextI18n")
        private fun setupMovieRating(
            binding: TvShowsTileVariant1Binding,
            rating: Double?,
        ) {
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                binding.ratingValue.text = "%.2f".format(value)
                binding.ratingBar.isEnabled = false;
            } else {
                binding.ratingValue.visibility = View.GONE
                binding.ratingBar.visibility = View.GONE
            }
        }

    }

    companion object {
        const val LOADING_ITEM = 0
        const val TV_SHOW_ITEM = 1
    }

}