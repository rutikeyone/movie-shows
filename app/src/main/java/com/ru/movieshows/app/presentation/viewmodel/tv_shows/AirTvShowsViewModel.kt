package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.tv_shows.AirTvShowsFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.tv_shows.entities.TvShowsEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class AirTvShowsViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel(), SimpleAdapterListener<TvShowsEntity> {

    val airTvShows: Flow<PagingData<TvShowsEntity>> = languageTagFlow.flatMapLatest {
        tvShowRepository
        .getPagedTheAirTvShows(it)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: TvShowsEntity) = navigateToTvShowDetails(data)

    private fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = AirTvShowsFragmentDirections.actionAirTvShowsFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): AirTvShowsViewModel
    }

}