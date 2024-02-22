package com.ru.movieshows.app.presentation.viewmodel.movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.movies.MoviesRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.movies.UpcomingMoviesFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.movies.entities.MovieEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalCoroutinesApi
class UpcomingMoviesViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(), SimpleAdapterListener<MovieEntity> {

    val upcomingMovies: Flow<PagingData<MovieEntity>> = languageTagFlow.flatMapLatest { language ->
        moviesRepository.getPagedUnComingMovies(language)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: MovieEntity){
        val id = data.id ?: return
        val action = UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): UpcomingMoviesViewModel
    }

}