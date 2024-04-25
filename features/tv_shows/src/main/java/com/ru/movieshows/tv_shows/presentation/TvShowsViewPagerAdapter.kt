package com.ru.movieshows.tv_shows.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.TvShowViewPagerItemBinding

class TvShowsViewPagerAdapter(
    private val tvShows: List<TvShow>,
    private val listener: SimpleAdapterListener<TvShow>,
) : RecyclerView.Adapter<TvShowsViewPagerAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowViewPagerItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = if (tvShows.size > 10) 10 else tvShows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = tvShows[position]
        holder.bind(item)
    }

    override fun onClick(view: View) {
        val tvShow = view.tag as TvShow
        listener.onClickItem(tvShow)
    }

    inner class Holder(private val binding: TvShowViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShow) {
            binding.root.tag = tvShow
            bindTitle(tvShow)
            bindMovieImage(tvShow)
        }

        private fun bindTitle(tvShow: TvShow) = with(binding.tvShowName) {
            val title = tvShow.name
            if (!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindMovieImage(tvShow: TvShow) = with(binding.tvShowBackground) {
            val backDrop = tvShow.backDrop
            val loadBackDrop =
                if (!backDrop.isNullOrEmpty()) backDrop
                else R.drawable.bg_poster_placeholder

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