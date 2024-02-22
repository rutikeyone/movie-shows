package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.tvshows.TvShowRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.tv_shows.TopRatedTvShowsFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.tvshows.entities.TvShowsEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatedTvShowsViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel(), SimpleAdapterListener<TvShowsEntity> {

    val topRatedTvShows = languageTagFlow.flatMapLatest {
        tvShowRepository
            .getPagedTheTopRatedTvShows(it)
    }.cachedIn(viewModelScope)

    private fun navigateToTvShowDetails(tvShow: TvShowsEntity) {
        val id = tvShow.id ?: return
        val action = TopRatedTvShowsFragmentDirections.actionTopRatedTvShowsFragmentToTvShowDetailsFragment(id)
        navigator.navigate(action)
    }

    override fun onClickItem(data: TvShowsEntity) = navigateToTvShowDetails(data)

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator) : TopRatedTvShowsViewModel
    }

}