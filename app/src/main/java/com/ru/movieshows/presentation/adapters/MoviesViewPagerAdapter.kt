package com.ru.movieshows.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.movies.MovieTileFragmentVariant1

class MoviesViewPagerAdapter(
    private val fragment: Fragment,
    private val movies: ArrayList<MovieEntity>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = if(movies.size < 8) movies.size else 8

    override fun createFragment(position: Int): Fragment {
        return MovieTileFragmentVariant1.newInstance(movies[position])
    }
}