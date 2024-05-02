package com.ru.movieshows.movies.presentation.details

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.GetMovieDetailsUseCase
import com.ru.movieshows.movies.domain.GetMovieReviewsUseCase
import com.ru.movieshows.movies.domain.GetSimilarMoviesUseCase
import com.ru.movieshows.movies.domain.GetVideosByIdUseCase
import com.ru.movieshows.movies.domain.entities.Movie
import com.ru.movieshows.movies.domain.entities.MovieDetails
import com.ru.movieshows.movies.domain.entities.Review
import com.ru.movieshows.movies.domain.entities.Video
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: MovieDetailsFragment.Screen,
    private val moviesRouter: MoviesRouter,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getVideosByIdUseCase: GetVideosByIdUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    private val titleStateFlow = MutableStateFlow<String>("")

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    val titleStateLiveValue = titleStateFlow
        .toLiveValue("")

    val movieSimpleAdapterListener = SimpleAdapterListener<Movie> {
        data -> launchMovieDetails(data)
    }

    val videoSimpleAdapterListener = SimpleAdapterListener<Video> { data ->
        launchVideo(data)
    }

    init {

        viewModelScope.launch {

            launch {
                languageTagFlow.collect {
                    getData(it)
                }
            }

        }

    }

    private suspend fun getData(language: String) {
        val firstPageIndex = 1

        loadScreenStateFlow.value = Container.Pending
        titleStateFlow.value = ""

        try {
            val movieDetails = getMovieDetailsUseCase.execute(language, args.id)
            val similarMovies =
                getSimilarMoviesUseCase.execute(language, firstPageIndex, args.id).results
            val videos = getVideosByIdUseCase.execute(language, args.id)
            val reviews = getMovieReviewsUseCase.execute(language, args.id).results

            val state = State(
                movieDetails = movieDetails,
                similarMovies = similarMovies,
                videos = videos,
                reviews = reviews,
            )

            loadScreenStateFlow.value = Container.Success(state)

            movieDetails.title?.let {
                titleStateFlow.value = it
            }

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }

    }

    fun tryGetMovieDetails() {
        viewModelScope.launch {
            getData(languageTag)
        }
    }

    fun launchReviews() {
        val state = loadScreenStateFlow.value.getOrNull() ?: return
        val id = state.movieDetails.id ?: return
    }

    private fun launchMovieDetails(movie: Movie) {}

    private fun launchVideo(video: Video) {}

    @AssistedFactory
    interface Factory {
        fun create(args: MovieDetailsFragment.Screen): MovieDetailsViewModel
    }

    data class State(
        val movieDetails: MovieDetails,
        val similarMovies: List<Movie>,
        val videos: List<Video>,
        val reviews: List<Review>,
    )

}