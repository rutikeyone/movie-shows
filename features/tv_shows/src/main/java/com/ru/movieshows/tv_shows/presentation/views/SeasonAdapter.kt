package com.ru.movieshows.tv_shows.presentation.views

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.tvshows.databinding.SeasonItemBinding

class SeasonAdapter(
    private val tvShowSeasons: List<Season>,
    private val listener: SimpleAdapterListener<Season>,
) : RecyclerView.Adapter<SeasonAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SeasonItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = tvShowSeasons.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val season = tvShowSeasons[position]
        holder.bindViews(season)
    }

    override fun onClick(view: View) {
        val season = view.tag as Season
        listener.onClickItem(season)
    }

    inner class Holder(
        private val binding: SeasonItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(season: Season) {
            binding.root.tag = season
            bindPosterView(season)
            bindLayoutParamsView()
        }

        private fun bindPosterView(season: Season) {
            with(binding.seasonImageView) {
                val posterPath = season.posterPath

                Glide
                    .with(context)
                    .load(posterPath)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

        private fun bindLayoutParamsView() = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(com.ru.movieshows.tvshows.R.dimen.dp_190),
            )
            this.layoutParams = layoutParams
        }

    }

}