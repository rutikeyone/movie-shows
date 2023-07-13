package com.ru.movieshows.presentation.viewmodel.movies

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.R
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.presentation.utils.PermissionIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
): BaseViewModel() {
    private val _state = MutableLiveData<MoviesState>()
    val state = _state.share()

    init {
        fetchMoviesData()
    }

    private fun fetchMoviesData() = viewModelScope.launch {
        _state.value = MoviesState.InPending
        try {
            val nowPlayingMovies = fetchNowPlayingMovies()
            _state.value  = MoviesState.Success(nowPlayingMovies)
        } catch (e: Exception) {
            _state.value = MoviesState.Failure(R.string.an_error_occurred_during_the_operation)
        }
    }

    private suspend fun fetchNowPlayingMovies(): ArrayList<MovieEntity> {
        val language = Locale.getDefault().displayLanguage
        val getMoviesNowPlayingResult = moviesRepository.getMoviesNowPlaying(page = 1, language = language)
        return getMoviesNowPlayingResult.getOrThrow()
    }
}