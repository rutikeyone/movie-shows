package com.ru.movieshows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.R
import com.ru.movieshows.domain.entity.GenreEntity

class GenresAdapter(private val genres: ArrayList<GenreEntity>) : RecyclerView.Adapter<GenresAdapter.GenresHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutInflater = inflater.inflate(R.layout.genre_tile, parent, false)
        return GenresHolder(layoutInflater)
    }

    override fun getItemCount(): Int = genres.size

    override fun onBindViewHolder(holder: GenresHolder, position: Int) {
        holder.bind(genres[position])
    }

    inner class GenresHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(R.id.value)

        fun bind(genre: GenreEntity){
            text.text = genre.name
        }
    }

}