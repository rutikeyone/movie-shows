package com.ru.movieshows.season.presentation

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.season.domain.GetPersonDetailsUseCase
import com.ru.movieshows.season.domain.GetPersonImagesUseCase
import com.ru.movieshows.season.domain.entities.Person
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonDetailsViewModel @AssistedInject constructor(
    @Assisted private val personId: String,
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    private val getPersonImagesUseCase: GetPersonImagesUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow.toLiveValue(Container.Pending)

    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getData(
                    language = language,
                )
            }
        }
    }

    fun toTryAgain() = debounce {
        viewModelScope.launch {
            getData(
                language = languageTag,
            )
        }
    }

    private suspend fun getData(
        language: String,
        silentMode: Boolean = false,
    ) {

        if (!silentMode) {
            loadScreenStateFlow.value = Container.Pending
        }

        try {
            val person = getPersonDetailsUseCase.execute(
                personId = personId,
                language = language,
            )

            val images = getPersonImagesUseCase.execute(
                personId = personId,
            )

            val state = State(
                person = person,
                images = images,
            )

            loadScreenStateFlow.value = Container.Success(state)

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }

    }

    fun updatePerson() = debounce {
        val loadState = loadScreenStateFlow.value

        if (loadState is Container.Success) {
            viewModelScope.launch {
                getData(
                    language = languageTag,
                    silentMode = true,
                )
            }
        }

    }

    data class State(
        val person: Person,
        val images: List<String>?,
    )

    @AssistedFactory
    interface Factory {
        fun create(personId: String): PersonDetailsViewModel
    }

}