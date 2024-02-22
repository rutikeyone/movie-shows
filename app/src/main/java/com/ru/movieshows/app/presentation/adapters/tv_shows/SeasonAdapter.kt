package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.SeasonItemBinding
import com.ru.movieshows.sources.tvshows.entities.SeasonEntity

class SeasonAdapter(
    private val seasons: ArrayList<SeasonEntity>,
    private val listener: SimpleAdapterListener<SeasonEntity>,
) : RecyclerView.Adapter<SeasonAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SeasonItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = seasons.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val season = seasons[position]
        holder.bind(season)
    }

    override fun onClick(view: View) {
        val season = view.tag as SeasonEntity
        listener.onClickItem(season)
    }

    inner class Holder(
        private val binding: SeasonItemBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(season: SeasonEntity) {
            binding.root.tag = season
            bindPosterUI(season)
            bindLayoutParamsUI()
        }

        private fun bindPosterUI(season: SeasonEntity) = with(binding.image) {
            val photo = season.poster
            if (!photo.isNullOrEmpty()) {
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

        private fun bindLayoutParamsUI() = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_190),
            )
            this.layoutParams = layoutParams
        }

    }

}