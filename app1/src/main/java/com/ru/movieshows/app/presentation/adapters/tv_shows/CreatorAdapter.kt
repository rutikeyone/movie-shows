package com.ru.movieshows.app.presentation.adapters.tv_shows

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.databinding.CreatorItemBinding
import com.ru.movieshows.sources.tvshows.entities.CreatorEntity

class CreatorAdapter(
    private val creators: ArrayList<CreatorEntity>,
    private val listener: SimpleAdapterListener<CreatorEntity>,
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
        holder.bind(creator)
    }

    override fun onClick(view: View) {
        val creator = view.tag as CreatorEntity
        listener.onClickItem(creator)
    }

    inner class Holder(
        private val binding: CreatorItemBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(creator: CreatorEntity) {
            binding.root.tag = creator
            bindPhotoImageUI(creator)
            bindLayoutParamsUI()
            bindNameUI(creator)
        }

        private fun bindNameUI(creator: CreatorEntity) = with(binding) {
            val creatorName = creator.name
            if (!creatorName.isNullOrEmpty()) {
                name.text = creatorName
                name.isVisible = true
            } else {
                name.isVisible = false
            }
        }

        private fun bindLayoutParamsUI() {
            val layoutParams = ActionBar.LayoutParams(
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
                binding.root.resources.getDimensionPixelOffset(R.dimen.dp_230),
            )
            binding.root.layoutParams = layoutParams
        }

        private fun bindPhotoImageUI(creator: CreatorEntity) = with(binding.image) {
            val photo = creator.photo
            val context = this.context
            if (!photo.isNullOrEmpty()) {
                Glide
                    .with(context)
                    .load(photo)
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

    }

}