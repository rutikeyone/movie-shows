package com.ru.movieshows.movies.presentation

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
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.VideoItemBinding
import com.ru.movieshows.movies.domain.entities.Video

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
        holder.bind(video)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Video>) {
        this.videos = data
        notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        val video = view.tag as Video
        listener.onClickItem(video)
    }

    class Holder(
        private val binding: VideoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.root.tag = video
            bindLayoutParamsUI()
            bindImageViewUI(video)
            bindNameUI(video)
        }

        private fun bindNameUI(video: Video) = with(binding.videoNameTextView) {
            val name = video.name
            if (!name.isNullOrEmpty()) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindImageViewUI(video: Video) {
            with(binding.videoImageView) {
                val image = video.image
                val loadImage =
                    if (image.isNullOrEmpty()) R.drawable.bg_poster_placeholder else image

                Glide
                    .with(context)
                    .load(loadImage)
                    .centerCrop()
                    .placeholder(R.drawable.bg_poster_placeholder)
                    .error(R.drawable.bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

        private fun bindLayoutParamsUI() {
            with(binding.root) {
                val layoutParams = ActionBar.LayoutParams(
                    resources.getDimensionPixelOffset(R.dimen.dp_120),
                    resources.getDimensionPixelOffset(R.dimen.dp_230),
                )
                this.layoutParams = layoutParams
            }
        }

    }

}