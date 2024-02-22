package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.TvShowTimeViewPagerItemBinding
import com.ru.movieshows.sources.tvshows.entities.TvShowsEntity

class TvShowsViewPagerAdapter(
    private val tvShows: ArrayList<TvShowsEntity>,
    private val listener: SimpleAdapterListener<TvShowsEntity>,
) : RecyclerView.Adapter<TvShowsViewPagerAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowTimeViewPagerItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = if(tvShows.size > 10) 10 else tvShows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = tvShows[position]
        holder.bind(item)
    }

    override fun onClick(view: View) {
        val tvShow = view.tag as TvShowsEntity
        listener.onClickItem(tvShow)
    }

    inner class Holder(private val binding: TvShowTimeViewPagerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) {
            binding.root.tag = tvShow
            bindTitleUI(tvShow)
            bindMovieImageUI(tvShow)
        }

        private fun bindTitleUI(tvShow: TvShowsEntity) = with(binding.tvShowName) {
            val title = tvShow.name
            if (!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindMovieImageUI(tvShow: TvShowsEntity) = with(binding.tvShowBackground) {
            val backDrop = tvShow.backDrop
            if (!backDrop.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(backDrop)
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .centerCrop()
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