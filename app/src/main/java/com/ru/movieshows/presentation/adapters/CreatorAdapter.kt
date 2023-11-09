package com.ru.movieshows.presentation.adapters

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.R
import com.ru.movieshows.databinding.CreatorTileBinding
import com.ru.movieshows.domain.entity.CreatorEntity

class CreatorAdapter(
    private val creators: ArrayList<CreatorEntity>
) : RecyclerView.Adapter<CreatorAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.creator_tile, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = creators.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val creator = creators[position]
        holder.bind(creator)
    }

    inner class Holder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(creator: CreatorEntity) {
            val binding = CreatorTileBinding.bind(view)
            setupImage(creator, binding)
            setupLayoutParams(binding)
            setupName(creator, binding)
        }
    }

    private fun setupName(
        creator: CreatorEntity,
        binding: CreatorTileBinding,
    ) = with(binding)  {
        val creatorName = creator.name ?: return
        name.text = creatorName
    }

    private fun setupLayoutParams(binding: CreatorTileBinding) {
        val layoutParams = ActionBar.LayoutParams(
            binding.root.resources.getDimensionPixelOffset(R.dimen.dp_120),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
    }

    private fun setupImage(
        creator: CreatorEntity,
        binding: CreatorTileBinding
    ) = with(binding) {
        val photo = creator.photo ?: return@with
        Glide
            .with(binding.root)
            .load(photo)
            .centerCrop()
            .placeholder(R.drawable.poster_placeholder_bg)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }

}