package com.ru.movieshows.tv_shows.presentation.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.TvShowViewPagerItemBinding

class TvShowsViewPagerAdapter(
    private val tvShows: List<TvShow>,
    private val listener: SimpleAdapterListener<TvShow>,
) : ListAdapter<TvShow, TvShowsViewPagerAdapter.Holder>(TvShowDiffCalculator()),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowViewPagerItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = if (tvShows.size > 10) 10 else tvShows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = tvShows[position]
        holder.bindViews(item)
    }

    override fun onClick(view: View) {
        val tvShow = view.tag as TvShow
        listener.onClickItem(tvShow)
    }

    inner class Holder(private val binding: TvShowViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(tvShow: TvShow) {
            binding.root.tag = tvShow
            bindTitleView(tvShow)
            bindMovieImageView(tvShow)
        }

        private fun bindTitleView(tvShow: TvShow) {
            with(binding.tvShowNameTextView) {
                val title = tvShow.name
                if (!title.isNullOrEmpty()) {
                    text = title
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        private fun bindMovieImageView(tvShow: TvShow) {
            with(binding.tvShowBackgroundImageView) {
                val loadBackDrop = tvShow.backDropPath

                Glide
                    .with(context)
                    .load(loadBackDrop)
                    .centerCrop()
                    .placeholder(R.drawable.bg_poster_placeholder)
                    .error(R.drawable.bg_poster_placeholder)
                    .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }

}