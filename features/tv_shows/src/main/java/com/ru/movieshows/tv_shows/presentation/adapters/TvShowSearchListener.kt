package com.ru.movieshows.tv_shows.presentation.adapters

import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch

interface TvShowSearchListener {

    fun onDeleteTvShowSearch(tvShowSearch: TvShowSearch)

    fun onSelectTvShowSearch(tvShowSearch: TvShowSearch)

}