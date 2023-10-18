package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.domain.entity.TvShowsEntity
import com.ru.movieshows.domain.repository.TvShowRepository
import com.ru.movieshows.domain.repository.exceptions.AppFailure
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsState
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: String,
    private val tvShowRepository: TvShowRepository,
) : BaseViewModel() {
    private val _state = MutableLiveData<TvShowDetailsState>(TvShowDetailsState.Pure)
    val state get() = _state.share()

    private val _title = MutableLiveData("")
    val title = _title.share()

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        _title.value = ""
        _state.value = TvShowDetailsState.InPending
        try {
            val tvShow = getTvShowById()
            _title.value = tvShow.name
            _state.value = TvShowDetailsState.Success(tvShow)
        } catch (e: AppFailure) {
            val failureState = TvShowDetailsState.Failure(e.headerResource(), e.errorResource())
            _state.value = failureState
        }
    }

    private suspend fun getTvShowById(): TvShowDetailsEntity {
        val fetchTvShowDetailsResult = tvShowRepository.getTvShowDetails(currentLanguage, movieId)
        return fetchTvShowDetailsResult.getOrThrow()
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: String): TvShowDetailsViewModel
    }
}