package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SeasonItemBinding
import com.ru.movieshows.sources.tv_shows.entities.SeasonEntity

class SeasonAdapter(
    private val seasons: ArrayList<SeasonEntity>,
    private val onTap: (SeasonEntity) -> Unit,
) : RecyclerView.Adapter<SeasonAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.season_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = seasons.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val season = seasons[position]
        holder.bind(season, onTap)
    }

    inner class Holder(
        private val view: View
    ): RecyclerView.ViewHolder(view) {

        fun bind(
            season: SeasonEntity,
            onTap: (SeasonEntity) -> Unit,
        ) {
            val binding =  SeasonItemBinding.bind(view).also {
                it.root.setOnClickListener {
                    onTap(season)
                }
            }
            bindPoster(season, binding)
        }

        private fun bindPoster(
            season: SeasonEntity,
            binding: SeasonItemBinding
        ) = with(binding.image) {
            val photo = season.poster
            if(!photo.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(photo)
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