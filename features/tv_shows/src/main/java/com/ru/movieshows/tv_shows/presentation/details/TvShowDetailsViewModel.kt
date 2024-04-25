package com.ru.movieshows.tv_shows.presentation.details

import com.ru.movieshows.core.presentation.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: TvShowDetailsFragment.Screen,
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(args: TvShowDetailsFragment.Screen): TvShowDetailsViewModel
    }

}