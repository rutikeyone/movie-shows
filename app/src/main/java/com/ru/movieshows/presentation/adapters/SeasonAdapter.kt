package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.SearchTileBinding
import com.ru.movieshows.databinding.SeasonTileBinding
import com.ru.movieshows.domain.entity.SeasonEntity
import com.ru.movieshows.domain.entity.TvShowsEntity

class SeasonAdapter(
    private val seasons: ArrayList<SeasonEntity>,
    private val onTap: (SeasonEntity) -> Unit,
    ) : RecyclerView.Adapter<SeasonAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.season_tile, parent, false)
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
            val binding =  SeasonTileBinding.bind(view)
            binding.root.setOnClickListener { onTap(season) }
            setupImage(season, binding)
        }

        private fun setupImage(
            season: SeasonEntity,
            binding: SeasonTileBinding
        ) = with(binding) {
            val photo = season.poster ?: return@with
            Glide
                .with(binding.root)
                .load(photo)
                .centerCrop()
                .into(image)
        }
    }
}