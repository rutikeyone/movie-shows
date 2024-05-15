package com.ru.movieshows.movies.presentation.popular

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.GetPagedPopularMoviesUseCase
import com.ru.movieshows.movies.domain.GetPagedTopRatedMoviesUseCase
import com.ru.movieshows.movies.domain.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val router: MoviesRouter,
    private val getPagedPopularMoviesUseCase: GetPagedPopularMoviesUseCase,
) : BaseViewModel(), SimpleAdapterListener<Movie> {

    @OptIn(ExperimentalCoroutinesApi::class)
    val popularMovies: Flow<PagingData<Movie>> = languageTagFlow.flatMapLatest { language ->
        getPagedPopularMoviesUseCase.execute(language)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: Movie) {
        router.launchMovieDetails(data)
    }

}