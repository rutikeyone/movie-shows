package com.ru.movieshows.season.presentation

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.season.domain.GetPersonDetailsUseCase
import com.ru.movieshows.season.domain.entities.Person
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonDetailsViewModel @AssistedInject constructor(
    @Assisted private val personId: String,
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow<Container<Person>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow.toLiveValue(Container.Pending)

    init {
        viewModelScope.launch {
            languageTagFlow.collect { language ->
                getPersonData(
                    language = language,
                )
            }
        }
    }

    fun toTryAgain() = debounce {
        viewModelScope.launch {
            getPersonData(
                language = languageTag,
            )
        }
    }

    private suspend fun getPersonData(
        language: String,
        silentMode: Boolean = false,
    ) {

        if (!silentMode) {
            loadScreenStateFlow.value = Container.Pending
        }

        try {
            val result = getPersonDetailsUseCase.execute(
                personId = personId,
                language = language,
            )

            loadScreenStateFlow.value = Container.Success(result)

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }

    }

    fun updatePerson() = debounce {
        val loadState = loadScreenStateFlow.value

        if (loadState is Container.Success) {
            viewModelScope.launch {
                getPersonData(
                    language = languageTag,
                    silentMode = true,
                )
            }
        }

    }

    @AssistedFactory
    interface Factory {
        fun create(personId: String): PersonDetailsViewModel
    }

}