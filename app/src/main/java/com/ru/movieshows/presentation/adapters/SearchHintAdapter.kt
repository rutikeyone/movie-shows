package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.databinding.SearchHintItemBinding

class SearchHintAdapter(
    private val searchItems: List<String?>,
    private val onTap: (Int) -> Unit,
) : RecyclerView.Adapter<SearchHintAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchHintItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = searchItems.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = searchItems[position]
        holder.bind(
            name = item,
            onTap = {
                onTap(position)
            }
        )
    }

    inner class Holder(
        private val binding: SearchHintItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            name: String?,
            onTap: () -> Unit,
        ) {
            configureName(name)
            binding.root.setOnClickListener { onTap() }
        }

        private fun configureName(name: String?) = with(binding.hintNameTextView) {
            if(!name.isNullOrEmpty()) {
                visibility = View.VISIBLE
                text = name
            } else {
                visibility = View.INVISIBLE
            }
        }
    }
}