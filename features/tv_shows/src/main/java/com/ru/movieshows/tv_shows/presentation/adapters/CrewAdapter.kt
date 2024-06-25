package com.ru.movieshows.tv_shows.presentation.adapters

import android.annotation.SuppressLint
import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.Crew
import com.ru.movieshows.tvshows.databinding.CrewItemBinding

class CrewAdapter(
    private val listener: SimpleAdapterListener<Crew>,
) : RecyclerView.Adapter<CrewAdapter.Holder>(), View.OnClickListener {

    private var crews = listOf<Crew>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CrewItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = crews.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = crews[position]
        holder.bind(item)
    }

    override fun onClick(v: View) {
        val crew = v.tag as Crew
        listener.onClickItem(crew)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(crews: List<Crew>) {
        this.crews = crews
        notifyDataSetChanged()
    }

    inner class Holder(
        private val binding: CrewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crew: Crew) {
            binding.root.tag = crew

            bindPhotoImageView(crew)
            bindLayoutParamsView()
            bindNameView(crew)
            bindJobView(crew)
        }

        private fun bindPhotoImageView(crew: Crew) {
            val loadProfilePath = crew.profilePath

            with(binding.image) {
                Glide
                    .with(this)
                    .load(loadProfilePath)
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
                    binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                    binding.root.resources.getDimensionPixelOffset(com.ru.movieshows.tvshows.R.dimen.dp_250),
                )
                this.layoutParams = layoutParams
            }
        }

        private fun bindNameView(crew: Crew) {
            val name = crew.name

            with(binding.nameTextView) {
                if (!name.isNullOrEmpty()) {
                    text = name
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

        private fun bindJobView(crew: Crew) {
            val job = crew.job

            with(binding.jobTextView) {
                if (!job.isNullOrEmpty()) {
                    text = job
                    isVisible = true
                } else {
                    isVisible = false
                }
            }
        }

    }

}