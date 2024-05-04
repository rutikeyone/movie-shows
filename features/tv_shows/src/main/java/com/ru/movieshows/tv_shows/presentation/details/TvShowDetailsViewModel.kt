package com.ru.movieshows.tv_shows.presentation.details

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.core.presentation.live.LiveEventValue
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetPersonDetailsUseCase
import com.ru.movieshows.tv_shows.domain.GetTvShowDetailsUseCase
import com.ru.movieshows.tv_shows.domain.GetTvShowReviewsUseCase
import com.ru.movieshows.tv_shows.domain.GetVideosByIdUseCase
import com.ru.movieshows.tv_shows.domain.entities.Creator
import com.ru.movieshows.tv_shows.domain.entities.Person
import com.ru.movieshows.tv_shows.domain.entities.Review
import com.ru.movieshows.tv_shows.domain.entities.Season
import com.ru.movieshows.tv_shows.domain.entities.TvShowDetails
import com.ru.movieshows.tv_shows.domain.entities.Video
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TvShowDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: TvShowDetailsFragment.Screen,
    private val router: TvShowsRouter,
    private val getTvShowReviewsUseCase: GetTvShowReviewsUseCase,
    private val getVideosByIdUseCase: GetVideosByIdUseCase,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    private val titleStateFlow = MutableStateFlow<String>("")

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    val titleStateLiveValue = titleStateFlow
        .toLiveValue("")

    private val _showPeopleDetailsEvent = liveEvent<Person>()

    val showPeopleDetailsEvent: LiveEventValue<Person> = _showPeopleDetailsEvent

    val videoSimpleAdapterListener = SimpleAdapterListener<Video> {
        launchVideo(it)
    }

    val personSimpleAdapterListener = SimpleAdapterListener<Creator> {
        val id = it.id
        id?.let {
            getPersonDetails(it)
        }
    }

    val seasonSimpleListener = SimpleAdapterListener<Season> {
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
        titleStateFlow.value = ""
        loadScreenStateFlow.value = Container.Pending

        try {
            val tvShowDetails = getTvShowDetailsUseCase.execute(language, args.id)
            val videos = getVideosByIdUseCase.execute(language, args.id)
            val reviewPagination = getTvShowReviewsUseCase.execute(language, args.id)
            val reviews = reviewPagination.results

            val state = State(
                tvShowDetails = tvShowDetails,
                videos = videos,
                reviews = reviews,
            )

            titleStateFlow.value = tvShowDetails.name ?: ""
            loadScreenStateFlow.value = Container.Success(state)

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }

    }

    fun tryGetMovieDetails() {
        viewModelScope.launch {
            getData(languageTag)
        }
    }

    private fun getPersonDetails(personId: String) {
        viewModelScope.launch {
            val personDetails = getPersonDetailsUseCase.execute(
                personId = personId,
                language = languageTag,
            )
            _showPeopleDetailsEvent.publish(personDetails)
        }
    }

    private fun launchVideo(video: Video) {}

    fun launchToTvShowReviews() {}

    fun launchToEpisodes(seriesId: String, seasonNumber: String) {}

    @AssistedFactory
    interface Factory {
        fun create(args: TvShowDetailsFragment.Screen): TvShowDetailsViewModel
    }

    data class State(
        val tvShowDetails: TvShowDetails,
        val videos: List<Video>,
        val reviews: List<Review>,
    )

}