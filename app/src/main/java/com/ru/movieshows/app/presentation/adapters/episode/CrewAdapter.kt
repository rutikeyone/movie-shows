package com.ru.movieshows.app.presentation.adapters.episode

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.CrewItemBinding
import com.ru.movieshows.sources.tv_shows.entities.CrewEntity

class CrewAdapter(
    private val crew: ArrayList<CrewEntity>,
    private val listener: Listener,
) : RecyclerView.Adapter<CrewAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CrewItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = crew.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = crew[position]
        holder.bind(item)
    }

    override fun onClick(v: View) {
        val crew = v.tag as CrewEntity
        listener.onChooseCrew(crew)
    }

    interface Listener {
        fun onChooseCrew(crew: CrewEntity)
    }

    inner class Holder(
        private val binding: CrewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crew: CrewEntity) {
            binding.root.tag = crew
            bindPhotoImage(crew.profilePath)
            bindLayoutParams()
            bindName(crew.name)
            bindJob(crew.job)
        }

        private fun bindPhotoImage(image: String?) = with(binding.image) {
            val context = this.context
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

        private fun bindLayoutParams() = with(binding.root) {
            val layoutParams = ActionBar.LayoutParams(
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_250),
            )
            this.layoutParams = layoutParams
        }

        private fun bindName(name: String?) = with(binding.nameTextView) {
            if(!name.isNullOrEmpty()) {
                text = name
                isVisible = true
            } else {
                isVisible = false
            }
        }

        private fun bindJob(job: String?) = with(binding.jobTextView) {
            if(!job.isNullOrEmpty()) {
                text = job
                isVisible = true
            } else {
                isVisible = false
            }
        }
    }

}