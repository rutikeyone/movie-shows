package com.ru.movieshows.tv_shows.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.SearchHistoryItemBinding

class TvShowSearchListAdapter(
    private val listener: TvShowSearchListener,
) : ListAdapter<TvShowSearch, TvShowSearchListAdapter.TvShowSearchHolder>(TvShowSearchDiffCalculator()),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowSearchHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchHistoryItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.deleteImageView.setOnClickListener(this)
        return TvShowSearchHolder(binding)
    }

    override fun onBindViewHolder(holder: TvShowSearchHolder, position: Int) {
        val movieSearch = getItem(position)
        holder.bindView(movieSearch)
    }

    override fun onClick(view: View) {
        val movie = view.tag as TvShowSearch
        val itemId = view.id

        when (itemId) {
            R.id.deleteImageView -> listener.onDeleteTvShowSearch(movie)
            else -> listener.onSelectTvShowSearch(movie)
        }
    }

    class TvShowSearchHolder(
        private val binding: SearchHistoryItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(tvShowSearch: TvShowSearch) {
            binding.root.tag = tvShowSearch
            binding.deleteImageView.tag = tvShowSearch

            val name = tvShowSearch.name

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