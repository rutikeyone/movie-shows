package com.ru.movieshows.presentation.viewmodel.tv_show_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.domain.entity.TvShowDetailsEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val tvShowId: String,
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
        languageTagFlow.collect {
            _title.value = ""
            _state.value = TvShowDetailsState.InPending
            try {
                val tvShow = getTvShowById(it)
                val videos = getVideosById(it)
                _title.value = tvShow.name
                _state.value = TvShowDetailsState.Success(tvShow, videos)
            } catch (e: AppFailure) {
                val failureState = TvShowDetailsState.Failure(e.headerResource(), e.errorResource())
                _state.value = failureState
            }
        }
    }

    private suspend fun getTvShowById(language: String): TvShowDetailsEntity {
        val fetchTvShowDetailsResult = tvShowRepository.getTvShowDetails(language, tvShowId)
        return fetchTvShowDetailsResult.getOrThrow()
    }

    private suspend fun getVideosById(language: String): ArrayList<VideoEntity> {
        val fetchVideosByIdResult = tvShowRepository.getVideosByMovieId(language, tvShowId)
        return fetchVideosByIdResult.getOrThrow()
    }

    fun navigateToVideo(video: VideoEntity) {
        //TODO
//        val action = NavigationIntent.toVideo(video)
//        navigationEvent.publishEvent(action)
    }

    override fun onCleared() {
        super.onCleared()
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: String): TvShowDetailsViewModel
    }
}