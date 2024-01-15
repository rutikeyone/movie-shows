package com.ru.movieshows.app.presentation.adapters

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.VideoItemBinding
import com.ru.movieshows.sources.movies.entities.VideoEntity

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

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: ArrayList<VideoEntity>) {
        this.videos = data
        notifyDataSetChanged()
    }

    class Holder(
        private val binding: VideoItemBinding,
        private val onTap: (VideoEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoEntity) = with(binding.root) {
            setOnClickListener { onTap(video) }
            bindLayoutParams(binding)
            bindImageView(video, binding)
            bindName(video, binding)
        }

        private fun bindName(video: VideoEntity, binding: VideoItemBinding) = with(binding.videoNameTextView) {
            val name = video.name
            if(!name.isNullOrEmpty()) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindImageView(video: VideoEntity, binding: VideoItemBinding) = with(binding.videoImageView){
            val image = video.image
            if(!image.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(image)
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

        private fun bindLayoutParams(binding: VideoItemBinding) = with(binding.root){
            val layoutParams = ActionBar.LayoutParams(
                resources.getDimensionPixelOffset(R.dimen.dp_120),
                resources.getDimensionPixelOffset(R.dimen.dp_230),
            )
            this.layoutParams = layoutParams
        }
    }

}