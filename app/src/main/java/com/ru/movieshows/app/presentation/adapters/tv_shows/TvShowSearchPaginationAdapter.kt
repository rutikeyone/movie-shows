package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.SearchItemBinding
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity

class TvShowSearchPaginationAdapter(
    private val listener: SimpleAdapterListener<TvShowsEntity>,
) : PagingDataAdapter<TvShowsEntity, TvShowSearchPaginationAdapter.Holder>(TvShowDiffItemCallback()), View.OnClickListener {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tvShow = getItem(position) ?: return
        holder.bind(tvShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onClick(view: View) {
        val item = view.tag as TvShowsEntity
        listener.onClickItem(item)
    }

    class Holder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) {
            binding.root.tag = tvShow
            bindTitleUI(tvShow)
            setupBackDropUI(tvShow)
            bindRatingUI(tvShow)
            bindDescriptionUI(tvShow)
        }

        private fun bindTitleUI(tvShow: TvShowsEntity) = with(binding.headerText) {
            val name = tvShow.name
            if (name != null) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindDescriptionUI(tvShow: TvShowsEntity) = with(binding) {
            val overview = tvShow.overview
            if (overview != null) {
                descriptionText.text = tvShow.overview
                descriptionText.isVisible = true
            } else {
                descriptionText.isVisible = false
            }
        }

        private fun bindRatingUI(tvShow: TvShowsEntity) = with(binding) {
            val rating = tvShow.rating
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat() / 2
                ratingValue.text = "%.2f".format(rating)
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

        private fun setupBackDropUI(tvShow: TvShowsEntity) = with(binding.imageView) {
            val poster = tvShow.poster
            if (!poster.isNullOrEmpty()) {
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
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .centerCrop()
                    .into(this)
            }
        }

    }

}