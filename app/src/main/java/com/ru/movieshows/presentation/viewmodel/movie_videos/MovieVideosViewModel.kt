package com.ru.movieshows.presentation.viewmodel.movie_videos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.Locale

class MovieVideosViewModel @AssistedInject constructor(
    @Assisted private val movieId: String,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel() {
    private val currentLanguage get() = Locale.getDefault().toLanguageTag()

    private val _state = MutableLiveData<MovieVideosState>(MovieVideosState.InPending)
    val state = _state.share()

    init {
        fetchVideos()
    }

    fun fetchVideos() = viewModelScope.launch {
        Log.e("TAG", movieId);
        val fetchVideos = moviesRepository.getVideosByMovieId(currentLanguage, movieId)
        fetchVideos.fold(::executeSuccessFetchVideos, ::executeFailureFetchVideos);
    }

    private fun executeFailureFetchVideos(throwable: Throwable) {
        if(throwable !is AppFailure) return
        val header = throwable.headerResource()
        val error = throwable.errorResource()
        val failureState = MovieVideosState.Failure(header, error)
        _state.value = failureState
    }

    private fun executeSuccessFetchVideos(videoEntities: ArrayList<VideoEntity>) {
        if(videoEntities.isNotEmpty()) {
            val successState = MovieVideosState.Success(videoEntities)
            _state.value = successState
        } else {
            val successEmptyState = MovieVideosState.SuccessEmpty
            _state.value = successEmptyState
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: String): MovieVideosViewModel
    }
}