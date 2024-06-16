package com.ru.movieshows.movies.presentation.upcoming

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.GetPagedUpcomingMoviesUseCase
import com.ru.movieshows.movies.domain.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    private val router: MoviesRouter,
    private val getPagedUpcomingMoviesUseCase: GetPagedUpcomingMoviesUseCase,
) : BaseViewModel(), SimpleAdapterListener<Movie> {

    @OptIn(ExperimentalCoroutinesApi::class)
    val upcomingMovies: Flow<PagingData<Movie>> = languageTagFlow.flatMapLatest { language ->
        getPagedUpcomingMoviesUseCase.execute(language)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: Movie) = debounce {
        router.launchMovieDetails(data)
    }

}