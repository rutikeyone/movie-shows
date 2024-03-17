package com.ru.movieshows.app.presentation.viewmodel.movies

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.movies.MoviesRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.movies.MovieDetailsFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.movies.state.MovieDetailsState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.movies.entities.MovieDetailsEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import com.ru.movieshows.sources.movies.entities.ReviewEntity
import com.ru.movieshows.sources.movies.entities.VideoEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    @Assisted private val navigator: Navigator,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(), DefaultLifecycleObserver {

    private val _state = MutableLiveData<MovieDetailsState>(MovieDetailsState.Empty)
    val state = _state.share()

    private val _title = MutableLiveData("")
    val titleState = _title.share()

    val movieSimpleAdapterListener = object : SimpleAdapterListener<MovieEntity> {
        override fun onClickItem(data: MovieEntity) {
            navigateToMovieDetails(data)
        }
    }

    val videoSimpleAdapterListener = object : SimpleAdapterListener<VideoEntity> {
        override fun onClickItem(data: VideoEntity) {
            navigateToVideo(data)
        }
    }

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            languageTagFlow.collect {
                _state.value = MovieDetailsState.Pending
                _title.value = ""
                try {
                    val movieDetails = fetchMovieDetails(it)
                    val similarMovies = fetchSimilarMovies(it)
                    val reviews = fetchReviews(it).second
                    val videos = fetchVideos(it)
                    _state.value = MovieDetailsState.Success(movieDetails, similarMovies, videos, reviews)
                    _title.value = movieDetails.title

                } catch (e: AppFailure) {
                    _state.value = MovieDetailsState.Failure(e.headerResource(), e.errorResource())
                }
            }
        }
    }

    private suspend fun fetchMovieDetails(language: String): MovieDetailsEntity {
        val fetchMovieDetailsResult = moviesRepository.getMovieDetails(
            movieId = movieId,
            language = language,
        )
        return fetchMovieDetailsResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchSimilarMovies(language: String): ArrayList<MovieEntity> {
        val page = 1
        val id = movieId.toString()
        val fetchSimilarMoviesResult = moviesRepository.getSimilarMovies(
            language = language,
            movieId = id,
            page = page
        )
        return fetchSimilarMoviesResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchReviews(language: String): Pair<Int, ArrayList<ReviewEntity>> {
        val firstPageIndex = 1
        val id = movieId.toString()
        val fetchReviewsResult = moviesRepository.getMovieReviews(
            language = language,
            movieId = id,
            page = firstPageIndex,
        )
        return fetchReviewsResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchVideos(language: String): ArrayList<VideoEntity> {
        val id = movieId.toString()
        val fetchVideosResult = moviesRepository.getVideosById(language, id)
        return fetchVideosResult.getOrElse {
            throw it
        }
    }

    fun navigateToReviews(value: ArrayList<ReviewEntity>) {
        val reviews = value.toTypedArray()
        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieReviewsFragment(movieId, reviews)
        navigator.navigate(action)
    }

    private fun navigateToMovieDetails(data: MovieEntity) {
        val id = data.id ?: return
        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(id)
        navigator.navigate(action)
    }

    private fun navigateToVideo(video: VideoEntity) {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToVideoActivity(video)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator, movieId: Int): MovieDetailsViewModel
    }

}