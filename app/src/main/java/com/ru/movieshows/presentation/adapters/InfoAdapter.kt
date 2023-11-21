package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.databinding.InfoItemBinding

class InfoAdapter(
    private val data: List<String?>
) : RecyclerView.Adapter<InfoAdapter.InfoHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = InfoItemBinding.inflate(inflater, parent, false)
        return InfoHolder(item)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class InfoHolder(
        private val binding: InfoItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String?) = with(binding){
            if(!data.isNullOrEmpty()) {
                value.text = data
                value.isVisible = true
            } else {
                value.isVisible = false
            }
        }

    }

}