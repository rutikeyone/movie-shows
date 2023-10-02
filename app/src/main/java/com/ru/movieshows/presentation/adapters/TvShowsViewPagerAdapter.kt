package com.ru.movieshows.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.movies.MovieTileFragmentVariant1
import com.ru.movieshows.presentation.screens.tvs.TvShowTimeVariant1

class TvShowsViewPagerAdapter(
    private val fragment: Fragment,
    private val tvShows: ArrayList<TvShowsEntity>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = if(tvShows.size < 10) tvShows.size else 10

    override fun createFragment(position: Int): Fragment {
        return TvShowTimeVariant1.newInstance(tvShows[position])
    }

}