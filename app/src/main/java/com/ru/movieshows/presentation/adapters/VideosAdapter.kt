package com.ru.movieshows.presentation.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.VideoItemBinding
import com.ru.movieshows.domain.entity.VideoEntity

class VideosAdapter (
    private val onTap: (VideoEntity) -> Unit,
) : RecyclerView.Adapter<VideosAdapter.Holder>() {

    private var videos = arrayListOf<VideoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VideoItemBinding.inflate(inflater, parent, false)
        return Holder(binding, onTap)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    fun updateData(data: ArrayList<VideoEntity>) {
        this.videos.clear()
        this.videos.addAll(data)
        notifyDataSetChanged()
    }

    class Holder(
        private val binding: VideoItemBinding,
        private val onTap: (VideoEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoEntity) {
            setupLayoutParams(binding)
            setupImageView(video, binding)
            setupName(video, binding)
            binding.root.setOnClickListener { onTap(video) }
        }

        private fun setupName(video: VideoEntity, binding: VideoItemBinding) {
            if (video.name != null) binding.videoNameTextView.text = video.name
        }

        private fun setupImageView(video: VideoEntity, binding: VideoItemBinding) {
            if (video.image != null) {
                Glide
                    .with(binding.root)
                    .load(video.image)
                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder_bg)
                    .error(R.drawable.poster_placeholder_bg)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.videoImageView)
            }
        }

        private fun setupLayoutParams(binding: VideoItemBinding) {
            val layoutParams = ActionBar.LayoutParams(
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.root.layoutParams = layoutParams
        }

    }

}