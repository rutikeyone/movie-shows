package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val item = inflater.inflate(R.layout.tv_show_time_view_pager_item, parent, false)
        return Holder(item, onTap)
    }

    override fun getItemCount(): Int = if(tvShows.size > 10) 10 else tvShows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = tvShows[position]
        holder.bind(item)    }

    inner class Holder(
        private val view: View,
        private val onTap: (TvShowsEntity) -> Unit,
        ) : RecyclerView.ViewHolder(view) {

        fun bind(tvShow: TvShowsEntity) {
            val binding = TvShowTimeViewPagerItemBinding.bind(view)
            setupTitle(binding, tvShow)
            setupMovieImage(binding, tvShow)
            binding.root.setOnClickListener { onTap(tvShow) }
        }

        private fun setupTitle(
            binding: TvShowTimeViewPagerItemBinding,
            tvShow: TvShowsEntity
        ) {
            val title = tvShow.name ?: return
            binding.tvShowName.text = title
        }

        private fun setupMovieImage(
            binding: TvShowTimeViewPagerItemBinding,
            tvShow: TvShowsEntity
        ) {
            val backDrop = tvShow.backDrop ?: return
            Glide
                .with(binding.root)
                .load(backDrop)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.tvShowBackground)
        }

    }

}