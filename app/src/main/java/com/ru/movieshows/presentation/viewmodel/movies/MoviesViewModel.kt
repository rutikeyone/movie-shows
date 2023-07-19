package com.ru.movieshows.presentation.viewmodel.movies

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.R
import com.ru.movieshows.domain.entity.GenreEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.GenresRepository
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.utils.PermissionIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val genresRepository: GenresRepository,
): BaseViewModel() {
    private val _state = MutableLiveData<MoviesState>()
    val state = _state.share()

    private val _tabIndex = MutableLiveData(0)
    val tabIndex get() = _tabIndex.value ?: 0

    init {
        fetchMoviesData()
    }

    fun fetchMoviesData() = viewModelScope.launch {
        _state.value = MoviesState.InPending
        try {
            val nowPlayingMovies = fetchNowPlayingMovies()
            val genres = fetchGenres()
            _state.value  = MoviesState.Success(nowPlayingMovies, genres)
        } catch (e: AppFailure) {
            _state.value = MoviesState.Failure(e.headerResource(), e.errorResource())
        }
    }

    fun changeTabIndex(value: Int){
        _tabIndex.value = value
    }

    private suspend fun fetchNowPlayingMovies(): ArrayList<MovieEntity> {
        val language = Locale.getDefault().toLanguageTag()
        val getMoviesNowPlayingResult = moviesRepository.getMoviesNowPlaying(page = 1, language = language)
        return getMoviesNowPlayingResult.getOrThrow()
    }

    private suspend fun fetchGenres(): ArrayList<GenreEntity> {
        val language = Locale.getDefault().toLanguageTag()
        val getGenresResult = genresRepository.getGenres(language)
        return getGenresResult.getOrThrow()
    }
}