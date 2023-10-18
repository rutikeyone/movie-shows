package com.ru.movieshows.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.movies.MovieTileFragmentVariant1

class MoviesViewPagerAdapter(
    private val fragment: Fragment,
    private val movies: ArrayList<MovieEntity>,
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = if(movies.size < 10) movies.size else 10

    override fun createFragment(position: Int): Fragment {
        val movie = movies[position]
        return MovieTileFragmentVariant1.newInstance(movie)
    }
}