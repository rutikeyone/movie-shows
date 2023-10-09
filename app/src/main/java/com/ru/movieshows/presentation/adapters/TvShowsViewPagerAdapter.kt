package com.ru.movieshows.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.movies.MovieTileFragmentVariant1
import com.ru.movieshows.presentation.screens.tvs.TvShowTimeVariant1
import com.ru.movieshows.presentation.utils.Listener

class TvShowsViewPagerAdapter(
    private val fragment: Fragment,
    private val tvShows: ArrayList<TvShowsEntity>,
    private val listener: Listener<TvShowsEntity>,
    ) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = if(tvShows.size < 10) tvShows.size else 10

    override fun createFragment(position: Int): Fragment {
        val tvShow = tvShows[position]
        return TvShowTimeVariant1.newInstance(tvShow, listener)
    }

}