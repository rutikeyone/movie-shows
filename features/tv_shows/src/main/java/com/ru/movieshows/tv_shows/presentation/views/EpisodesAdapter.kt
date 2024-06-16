package com.ru.movieshows.tv_shows.presentation.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tvshows.databinding.EpisodeItemBinding

class EpisodesAdapter(
    private val listener: SimpleAdapterListener<Episode>,
) : RecyclerView.Adapter<EpisodesAdapter.Holder>(), View.OnClickListener {

    private var episodes = listOf<Episode>()

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

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(episodes: List<Episode>) {
        this.episodes = episodes
        notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        val episode = v.tag as Episode
        listener.onClickItem(episode)
    }

    inner class Holder(
        private val binding: EpisodeItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episode) {
            binding.root.tag = episode
            bindEpisodeNameView(episode)
            bindEpisodeImageView(episode)
            bindRatingView(episode.rating)
        }

        private fun bindEpisodeImageView(episode: Episode) {
            with(binding.episodeImageView) {
                val loadStillPath = episode.stillPath

                Glide
                    .with(context)
                    .load(loadStillPath)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

        private fun bindEpisodeNameView(episode: Episode) {
            with(binding.tvShowNameTextView) {
                val title = episode.name
                if (!title.isNullOrEmpty()) {
                    text = title
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun bindRatingView(rating: Double?) {
            with(binding) {
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

}