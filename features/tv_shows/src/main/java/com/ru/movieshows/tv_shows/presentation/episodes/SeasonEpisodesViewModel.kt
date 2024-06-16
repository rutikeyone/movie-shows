package com.ru.movieshows.tv_shows.presentation.episodes

import android.annotation.SuppressLint
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.EmptyException
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetEpisodesBySeasonUseCase
import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tvshows.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SeasonEpisodesViewModel @AssistedInject constructor(
    @Assisted private val args: SeasonEpisodesFragment.Screen,
    private val getEpisodesBySeasonUseCase: GetEpisodesBySeasonUseCase,
    private val router: TvShowsRouter,
) : BaseViewModel(), SimpleAdapterListener<Episode> {

    private val loadScreenStateFlow = MutableStateFlow<Container<List<Episode>>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getEpisodesBySeason(
                    language = language
                )
            }
        }
    }

    fun toTryAgain() {
        debounce {
            viewModelScope.launch {
                getEpisodesBySeason(languageTag)
            }
        }
    }

    @SuppressLint("ResourceType")
    private suspend fun getEpisodesBySeason(language: String) {
        loadScreenStateFlow.value = Container.Pending

        try {
            val seasonNumber = args.seasonNumber
            val seriesId = args.seriesId

            val result = getEpisodesBySeasonUseCase.execute(
                language = language,
                seasonNumber = seasonNumber,
                seriesId = seriesId,
            )

            if (result.isNotEmpty()) {
                loadScreenStateFlow.value = Container.Success(result)
            } else {
                val emptyException = EmptyException(R.string.the_list_of_episodes_is_empty)

                loadScreenStateFlow.value = Container.Error(emptyException)
            }


        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }
    }

    override fun onClickItem(data: Episode) {
        val seriesId = args.seriesId
        val seasonNumber = args.seasonNumber
        val episodeNumber = data.episodeNumber ?: return

        router.launchEpisodeDetails(
            seriesId = seriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            args: SeasonEpisodesFragment.Screen,
        ): SeasonEpisodesViewModel
    }


}