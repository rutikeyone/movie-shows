package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.TvShowTimeViewPagerItemBinding
import com.ru.movieshows.domain.entity.TvShowsEntity

class TvShowsViewPagerAdapter(
    private val tvShows: ArrayList<TvShowsEntity>,
    private val onTap: (TvShowsEntity) -> Unit,
    ) : RecyclerView.Adapter<TvShowsViewPagerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TvShowTimeViewPagerItemBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    override fun getItemCount(): Int = if(tvShows.size > 10) 10 else tvShows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = tvShows[position]
        holder.bind(item)    }

    inner class Holder(
        private val binding: TvShowTimeViewPagerItemBinding,
        private val onTap: (TvShowsEntity) -> Unit,
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowsEntity) = with(binding.root) {
            setOnClickListener { onTap(tvShow) }
            bindTitle(binding, tvShow)
            bindMovieImage(binding, tvShow)
        }

        private fun bindTitle(
            binding: TvShowTimeViewPagerItemBinding,
            tvShow: TvShowsEntity
        ) = with(binding.tvShowName) {
            val title = tvShow.name
            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = false
            } else {
                isVisible = true
            }
        }

        private fun bindMovieImage(
            binding: TvShowTimeViewPagerItemBinding,
            tvShow: TvShowsEntity
        ) = with(binding.tvShowBackground){
            val backDrop = tvShow.backDrop
            if(!backDrop.isNullOrEmpty()) {
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
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }

}