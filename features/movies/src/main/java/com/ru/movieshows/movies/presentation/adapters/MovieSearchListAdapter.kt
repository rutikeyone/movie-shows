package com.ru.movieshows.movies.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.movies.R
import com.ru.movieshows.movies.databinding.SearchHistoryItemBinding
import com.ru.movieshows.movies.domain.entities.MovieSearch

class MovieSearchListAdapter(
    private val listener: MovieSearchListener,
) :
    ListAdapter<MovieSearch, MovieSearchListAdapter.MovieSearchHolder>(MovieSearchDiffCalculator()),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchHistoryItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.deleteImageView.setOnClickListener(this)
        return MovieSearchHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieSearchHolder, position: Int) {
        val movieSearch = getItem(position)
        holder.bindView(movieSearch)
    }

    override fun onClick(view: View) {
        val movie = view.tag as MovieSearch
        val itemId = view.id

        when (itemId) {
            R.id.deleteImageView -> listener.onDeleteMovieSearch(movie)
            else -> listener.onSelectMovieSearch(movie)
        }
    }


    class MovieSearchHolder(
        private val binding: SearchHistoryItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(movieSearch: MovieSearch) {
            binding.root.tag = movieSearch
            binding.deleteImageView.tag = movieSearch

            val name = movieSearch.name

            with(binding.nameTextView) {
                if (!name.isNullOrEmpty()) {
                    visibility = View.VISIBLE
                    text = name
                } else {
                    visibility = View.INVISIBLE
                }
            }

        }

    }
}