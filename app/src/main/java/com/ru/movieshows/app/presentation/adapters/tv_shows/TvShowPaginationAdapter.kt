package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.TvShowsItemBinding
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

class TvShowPaginationAdapter(
    private val onTap: (TvShowsEntity) -> Unit,
) : PagingDataAdapter<TvShowsEntity, TvShowPaginationAdapter.Holder>(TvShowDiffItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) TV_SHOW_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bind(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowsItemBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    class Holder(
        private val binding: TvShowsItemBinding,
        private val onTap: (TvShowsEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) {
            binding.root.setOnClickListener { onTap(tvShow) }
            bindTvShowName(tvShow, binding)
            bindTvShowImage(tvShow, binding)
            bindMovieRating(binding, tvShow.rating)
        }

        private fun bindTvShowImage(
            tvShow: TvShowsEntity,
            binding: TvShowsItemBinding
        ) = with(binding.tvShowsMovieImage) {
            val poster = tvShow.poster
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

        private fun bindTvShowName(
            tvShow: TvShowsEntity,
            binding: TvShowsItemBinding
        ) = with(binding.tvShowNameTextView) {
            val title = tvShow.name
            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindMovieRating(
            binding: TvShowsItemBinding,
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

    }

    companion object {
        const val LOADING_ITEM = 0
        const val TV_SHOW_ITEM = 1
    }

}