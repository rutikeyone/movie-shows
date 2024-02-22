package com.ru.movieshows.app.presentation.adapters.episode

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.databinding.EpisodeItemBinding
import com.ru.movieshows.sources.tvshows.entities.EpisodeEntity

class EpisodesAdapter(
    private val episodes: ArrayList<EpisodeEntity>,
    private val listener: SimpleAdapterListener<EpisodeEntity>,
) : RecyclerView.Adapter<EpisodesAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EpisodeItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val episode = episodes[position]
        holder.bind(episode)
    }

    override fun onClick(v: View) {
        val episode = v.tag as EpisodeEntity
        listener.onClickItem(episode)
    }

    inner class Holder(
        private val binding: EpisodeItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: EpisodeEntity) {
            binding.root.tag = episode
            //bindLayoutParamsUI()
            bindEpisodeNameUI(episode)
            bindEpisodeImageUI(episode)
            bindRatingUI(episode.rating)
        }

        private fun bindEpisodeImageUI(episode: EpisodeEntity) = with(binding.episodeImageView) {
            val poster = episode.stillPath
            val context = this.context
            if(!poster.isNullOrEmpty()) {
                Glide
                    .with(context)
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

        private fun bindEpisodeNameUI(episode: EpisodeEntity) = with(binding.tvShowNameTextView) {
            val title = episode.name
            if(!title.isNullOrEmpty()) {
                text = title
                isVisible = true
            } else {
                isVisible = false
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindRatingUI(rating: Double?) = with(binding) {
            ratingBar.isEnabled = false
            if (rating != null && rating > 0) {
                val value = rating.toFloat()
                ratingValue.text = "%.2f".format(value)
                ratingValue.isVisible = true
                ratingBar.isVisible = true
                ratingContainer.isVisible = true
            } else {
                ratingValue.isVisible = false
                ratingBar.isVisible = false
                ratingContainer.isVisible = false
            }
        }

    }

}