package com.ru.movieshows.app.presentation.adapters

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.VideoItemBinding
import com.ru.movieshows.sources.movies.entities.VideoEntity

class VideosAdapter (
    private val listener: SimpleAdapterListener<VideoEntity>,
) : RecyclerView.Adapter<VideosAdapter.Holder>(), View.OnClickListener {

    private var videos = arrayListOf<VideoEntity>()

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
    fun updateData(data: ArrayList<VideoEntity>) {
        this.videos = data
        notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        val video = view.tag as VideoEntity
        listener.onClickItem(video)
    }

    class Holder(
        private val binding: VideoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoEntity) {
            binding.root.tag = video
            bindLayoutParamsUI()
            bindImageViewUI(video)
            bindNameUI(video)
        }

        private fun bindNameUI(video: VideoEntity) = with(binding.videoNameTextView) {
            val name = video.name
            if (!name.isNullOrEmpty()) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindImageViewUI(video: VideoEntity) = with(binding.videoImageView) {
            val image = video.image
            if (!image.isNullOrEmpty()) {
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