package com.ru.movieshows.presentation.viewmodel.popular_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.popular_movies.PopularMoviesFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class PopularMoviesViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val moviesRepository: MoviesRepository
): BaseViewModel() {

    val popularMovies: Flow<PagingData<MovieEntity>> = languageTagFlow.flatMapLatest {
        moviesRepository.getPagedPopularMovies(it)
    }.cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): PopularMoviesViewModel
    }

}