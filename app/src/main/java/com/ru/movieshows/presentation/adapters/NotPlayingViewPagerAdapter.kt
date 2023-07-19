package com.ru.movieshows.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.movies.MovieTileVariant1Fragment

class NotPlayingViewPagerAdapter(private val fragment: Fragment, private val nowPlayingMovies: ArrayList<MovieEntity>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = if(nowPlayingMovies.size < 5) nowPlayingMovies.size else 5

    override fun createFragment(position: Int): Fragment {
        return MovieTileVariant1Fragment.newInstance(nowPlayingMovies[position])
    }
}