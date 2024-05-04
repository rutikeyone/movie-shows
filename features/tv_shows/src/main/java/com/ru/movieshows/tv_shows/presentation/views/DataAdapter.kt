package com.ru.movieshows.tv_shows.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.tvshows.databinding.DataItemBinding

class DataAdapter(
    private val data: List<String?>,
) : RecyclerView.Adapter<DataAdapter.DataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = DataItemBinding.inflate(inflater, parent, false)
        return DataHolder(item)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        val item = data[position]
        holder.bindViews(data = item)
    }

    inner class DataHolder(
        private val binding: DataItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(data: String?) {
            with(binding) {
                if (!data.isNullOrEmpty()) {
                    valueTextView.text = data
                    valueTextView.isVisible = true
                } else {
                    valueTextView.isVisible = false
                }
            }
        }

    }

}