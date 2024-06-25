package com.ru.movieshows.tv_shows.presentation.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.domain.entities.Creator
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.tvshows.databinding.CreatorItemBinding

class CreatorAdapter(
    private val creators: List<Creator>,
    private val listener: SimpleAdapterListener<Creator>,
) : RecyclerView.Adapter<CreatorAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CreatorItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = creators.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val creator = creators[position]
        holder.bindViews(creator)
    }

    override fun onClick(view: View) {
        val creator = view.tag as Creator
        listener.onClickItem(creator)
    }

    inner class Holder(
        private val binding: CreatorItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(creator: Creator) {
            binding.root.tag = creator
            bindPhotoImageView(creator)
            bindLayoutParamsView()
            bindNameView(creator)
        }

        private fun bindNameView(creator: Creator) {
            with(binding) {
                val creatorName = creator.name

                if (!creatorName.isNullOrEmpty()) {
                    nameTextView.text = creatorName
                    nameTextView.isVisible = true
                } else {
                    nameTextView.isVisible = false
                }
            }
        }

        private fun bindLayoutParamsView() {
            val layoutParams = ActionBar.LayoutParams(
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                binding.root.resources.getDimensionPixelOffset(com.ru.movieshows.tvshows.R.dimen.dp_230),
            )
            binding.root.layoutParams = layoutParams
        }

        private fun bindPhotoImageView(creator: Creator) {
            with(binding.creatorImageView) {
                val loadBackDrop = creator.photoPath
                val context = this.context

                Glide
                    .with(context)
                    .load(loadBackDrop)
                    .centerCrop()
                    .placeholder(R.drawable.core_presentation_bg_poster_placeholder)
                    .error(R.drawable.core_presentation_bg_poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }

    }

}