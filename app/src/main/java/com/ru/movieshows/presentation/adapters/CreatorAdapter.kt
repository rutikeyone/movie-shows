package com.ru.movieshows.presentation.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.CreatorItemBinding
import com.ru.movieshows.domain.entity.CreatorEntity

class CreatorAdapter(
    private val creators: ArrayList<CreatorEntity>
) : RecyclerView.Adapter<CreatorAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.creator_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = creators.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val creator = creators[position]
        holder.bind(creator)
    }

    inner class Holder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(creator: CreatorEntity) {
            val binding = CreatorItemBinding.bind(view)
            bindPhotoImage(creator, binding)
            bindLayoutParams(binding)
            bindName(creator, binding)
        }
    }

    private fun bindName(
        creator: CreatorEntity,
        binding: CreatorItemBinding,
    ) = with(binding)  {
        val creatorName = creator.name
        if(!creatorName.isNullOrEmpty()) {
            name.text = creatorName
            name.isVisible = true
        } else {
            name.isVisible = false
        }
    }

    private fun bindLayoutParams(binding: CreatorItemBinding) = with(binding.root) {
        val layoutParams = ActionBar.LayoutParams(binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120), ViewGroup.LayoutParams.WRAP_CONTENT)
        this.layoutParams = layoutParams
    }

    private fun bindPhotoImage(
        creator: CreatorEntity,
        binding: CreatorItemBinding
    ) = with(binding.image) {
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
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }
}