package com.ru.movieshows.movies.presentation.search

import androidx.paging.cachedIn
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.DeleteMovieSearchUseCase
import com.ru.movieshows.movies.domain.GetAllMoviesSearchUseCase
import com.ru.movieshows.movies.domain.InsertMovieSearchUseCase
import com.ru.movieshows.movies.domain.SearchPagedMoviesUseCase
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.MovieSearch
import com.ru.movieshows.movies.presentation.adapters.MovieSearchListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MovieSearchViewModel @Inject constructor(
    private val searchPagedMoviesUseCase: SearchPagedMoviesUseCase,
    private val insertMovieSearchUseCase: InsertMovieSearchUseCase,
    private val deleteMovieSearchUseCase: DeleteMovieSearchUseCase,
    private val getAllMoviesSearchUseCase: GetAllMoviesSearchUseCase,
    private val router: MoviesRouter,
) : BaseViewModel(), SimpleAdapterListener<Movie>, MovieSearchListener {

    val moviesSearchStateFlow: Flow<List<MovieSearch>> = languageTagFlow.flatMapLatest {
        getAllMoviesSearchUseCase.execute(it)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val queryStateFlow = MutableStateFlow("")

    private val queryStateLiveValue = queryStateFlow
        .toLiveValue("")

    private val nullableQueryStateFlow: Flow<String?> = queryStateFlow.map { value ->
        return@map value.ifEmpty {
            null
        }
    }

    val queryStateValue: String?
        get() {
            val queryValue = queryStateLiveValue.value
            return queryValue.ifEmpty {
                null
            }
        }

    val isSearchMode: Boolean
        get() = !queryStateValue.isNullOrEmpty()

    val searchMoviesPagedFlowState = combine(
        languageTagFlow,
        nullableQueryStateFlow,
    ) { language, query ->
        State(
            language = language,
            query = query,
        )
    }.flatMapLatest {
        searchPagedMoviesUseCase.execute(it.language, it.query)
    }.cachedIn(viewModelScope)

    fun changeQuery(query: String) {
        if (queryStateValue != query) {
            queryStateFlow.value = query
        }
    }

    override fun onClickItem(data: Movie) = debounce {
        val locale = languageTag

        viewModelScope.launch {
            insertMovieSearchUseCase.execute(data, locale)
            router.launchMovieDetails(data.id)
        }
    }

    override fun onDeleteMovieSearch(movieSearch: MovieSearch) = debounce {
        viewModelScope.launch {
            deleteMovieSearchUseCase.execute(movieSearch.id)
        }
    }

    override fun onSelectMovieSearch(movieSearch: MovieSearch) = debounce {
        val id = movieSearch.movieId ?: return@debounce
        router.launchMovieDetails(id)
    }

    data class State(
        val language: String,
        val query: String?,
    )

}