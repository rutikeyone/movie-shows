package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ru.movieshows.R
import com.ru.movieshows.databinding.VideoImageTileBinding
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.presentation.utils.getYouTubeId

class VideosAdapter(
    private val video: ArrayList<VideoEntity>,
    private val onTap: (VideoEntity) -> Unit,
) : RecyclerView.Adapter<VideosAdapter.VideosHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosHolder {
        val inflater = LayoutInflater.from(parent.context)
        val tile = inflater.inflate(R.layout.video_image_tile, parent, false)
        return VideosHolder(tile, onTap)
    }
    override fun getItemCount(): Int = video.size

    override fun onBindViewHolder(holder: VideosHolder, position: Int) = holder.bind(video[position])

    class VideosHolder(
        private val view: View,
        private val onTap: (VideoEntity) -> Unit,
    ): RecyclerView.ViewHolder(view) {
        fun bind(video: VideoEntity) {
            val binding = VideoImageTileBinding.bind(view)
            setupPoster(video, binding)
            setupHeader(video, binding)
            binding.root.setOnClickListener{ onTap(video) }
        }

        private fun setupHeader(
            video: VideoEntity,
            binding: VideoImageTileBinding,
        ) {
            val canShowHeader = video.name?.isNotEmpty() ?: false
            binding.headerVideoView.isVisible = canShowHeader
            binding.headerVideoView.text = video.name
        }

        private fun setupPoster(
            video: VideoEntity,
            binding: VideoImageTileBinding,
        ) {
            if (video.key != null) {
                val url: String = view.resources.getString(R.string.youtube_url) + video.key
                val imageId = url.getYouTubeId()
                val image =
                    view.resources.getString(R.string.thumbnail_firstPart) + imageId + view.resources.getString(
                        R.string.thumbnail_secondPart
                    )
                Glide
                    .with(binding.root)
                    .load(image)
                    .centerCrop()
                    .into(binding.posterImageView)
            }
        }
    }
}