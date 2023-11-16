package com.ru.movieshows.presentation.viewmodel.movie_details

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(), DefaultLifecycleObserver {
    private val _state = MutableLiveData<MovieDetailsState>(MovieDetailsState.InPending)
    val state = _state.share()
    private val _title = MutableLiveData<String?>("")
    val title = _title.share()

    init {
        fetchData()
    }

    fun fetchData() = viewModelScope.launch {
        currentLanguage.collect {
            _state.value = MovieDetailsState.InPending
            _title.value = ""
            try {
                val movieDetails = fetchMovieDetails(it)
                val similarMovies = fetchSimilarMovies(it)
                val reviews = fetchReviews(it)
                val videos = fetchVideos(it)
                _state.value = MovieDetailsState.Success(movieDetails, similarMovies, videos, reviews)
                _title.value = movieDetails.title

            } catch (e: AppFailure) {
                _state.value = MovieDetailsState.Failure(e.headerResource(), e.errorResource())
            }
        }
    }

    private suspend fun fetchMovieDetails(language: String): MovieDetailsEntity {
        val fetchMovieDetailsResult = moviesRepository.getMovieDetails(movieId, language)
        return fetchMovieDetailsResult.getOrThrow()
    }

    private suspend fun fetchSimilarMovies(language: String): ArrayList<MovieEntity> {
        val page = 1
        val id = movieId.toString()
        val fetchSimilarMoviesResult = moviesRepository.getSimilarMovies(language, id, page)
        return fetchSimilarMoviesResult.getOrThrow()
    }

    private suspend fun fetchReviews(language: String): ArrayList<ReviewEntity> {
        val id = movieId.toString()
        val fetchReviewsResult = moviesRepository.getMovieReviews(language, id, 1)
        return fetchReviewsResult.getOrThrow()
    }

    private suspend fun fetchVideos(language: String): ArrayList<VideoEntity> {
        val id = movieId.toString()
        val fetchVideosResult = moviesRepository.getVideosByMovieId(language, id)
        return fetchVideosResult.getOrThrow()
    }

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = NavigationIntent.toMovieDetails(id)
        navigationEvent.publishEvent(action)
    }

    fun navigateToReviews(reviews: ArrayList<ReviewEntity>) {
        val action = NavigationIntent.toReviews(reviews, movieId)
        navigationEvent.publishEvent(action)
    }

    fun navigateToVideo(video: VideoEntity) {
        val intent = NavigationIntent.toVideo(video)
        navigationEvent.publishEvent(intent)
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieDetailsViewModel
    }
}