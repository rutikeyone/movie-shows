package com.ru.movieshows.movies.presentation.top_rated

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.GetPagedTopRatedMoviesUseCase
import com.ru.movieshows.movies.domain.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TopRatedMoviesViewModel @Inject constructor(
    private val router: MoviesRouter,
    private val getPagedTopRatedMoviesUseCase: GetPagedTopRatedMoviesUseCase,
) : BaseViewModel(), SimpleAdapterListener<Movie> {

    @OptIn(ExperimentalCoroutinesApi::class)
    val topRatedMovies: Flow<PagingData<Movie>> = languageTagFlow.flatMapLatest { language ->
        getPagedTopRatedMoviesUseCase.execute(language)
    }.cachedIn(viewModelScope)

    override fun onClickItem(data: Movie) = debounce {
        router.launchMovieDetails(data.id)
    }

}