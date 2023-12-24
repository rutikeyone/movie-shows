package com.ru.movieshows.presentation.viewmodel.top_rated_tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.presentation.screens.top_rated_tv_shows.TopRatedTvShowsFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatedTvShowsViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {

    val topRatedTvShows = languageTagFlow.flatMapLatest {
        tvShowRepository
            .getPagedTheTopRatedTvShows(it)
    }.cachedIn(viewModelScope)

    fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = TopRatedTvShowsFragmentDirections.actionTopRatedTvShowsFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator) : TopRatedTvShowsViewModel
    }
}