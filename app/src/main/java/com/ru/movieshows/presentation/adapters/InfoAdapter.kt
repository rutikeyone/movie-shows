package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.R

class InfoAdapter(private val data: List<String?>) : RecyclerView.Adapter<InfoAdapter.InfoHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutInflater = inflater.inflate(R.layout.info_item, parent, false)
        return InfoHolder(layoutInflater)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class InfoHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(R.id.value)

        fun bind(data: String?){
            text.text = data
        }
    }

}