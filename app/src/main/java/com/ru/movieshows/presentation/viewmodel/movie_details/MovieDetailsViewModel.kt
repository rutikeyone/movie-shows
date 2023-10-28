package com.ru.movieshows.presentation.viewmodel.movie_details

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieDetailsEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.movie_details.MovieDetailsFragmentDirections
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
                _state.value = MovieDetailsState.Success(movieDetails, similarMovies, reviews)
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

    fun navigateToMovieDetails(movie: MovieEntity){
        if(movie.id == null) return;
        navigationEvent.publishEvent(NavigationIntent.To(MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(movie.id)))
    }

    fun navigateToReviews(reviews: ArrayList<ReviewEntity>) {
        val navigationIntent = NavigationIntent.To(
            MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieReviewsFragment(reviews.toTypedArray(), movieId)
        )
        navigationEvent.publishEvent(navigationIntent)
    }

    @AssistedFactory
    interface Factory {
        fun create(movieId: Int): MovieDetailsViewModel
    }
}