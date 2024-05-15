package com.ru.movieshows.tv_shows.presentation.on_the_air_tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.tvshows.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnTheAirTvShowsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_on_the_air_tv_shows, container, false)

}