package com.ru.movieshows.tv_shows.presentation.views

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.Video
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.tvshows.databinding.VideoItemBinding

class VideosAdapter(
    private val listener: SimpleAdapterListener<Video>,
) : RecyclerView.Adapter<VideosAdapter.Holder>(), View.OnClickListener {

    private var videos = listOf<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VideoItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val video = videos[position]
        holder.bindViews(video)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(videos: List<Video>) {
        this.videos = videos
        notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        val video = view.tag as Video
        listener.onClickItem(video)
    }

    class Holder(
        private val binding: VideoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(video: Video) {
            binding.root.tag = video
            bindLayoutParamsView()
            bindImageView(video)
            bindNameView(video)
        }

        private fun bindNameView(video: Video) = with(binding.videoNameTextView) {
            val name = video.name
            if (!name.isNullOrEmpty()) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindImageView(video: Video) {
            with(binding.videoImageView) {
                val loadImagePath = video.imagePath

                Glide
                    .with(context)
                    .load(loadImagePath)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

        private fun bindLayoutParamsView() {
            with(binding.root) {
                val layoutParams = ActionBar.LayoutParams(
                    resources.getDimensionPixelOffset(R.dimen.dp_120),
                    resources.getDimensionPixelOffset(com.ru.movieshows.tvshows.R.dimen.dp_230),
                )
                this.layoutParams = layoutParams
            }
        }

    }

}