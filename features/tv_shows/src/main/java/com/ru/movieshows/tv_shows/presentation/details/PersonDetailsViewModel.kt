package com.ru.movieshows.tv_shows.presentation.details

import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.tv_shows.domain.GetPersonDetailsUseCase
import com.ru.movieshows.tv_shows.domain.entities.Person
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class PersonDetailsViewModel @AssistedInject constructor(
    @Assisted private val initialPerson: Person,
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
) : BaseViewModel() {

    private val loadScreenStateFlow = MutableStateFlow(initialPerson)

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(initialPerson)

    fun getOrUpdatePerson() = debounce {
        viewModelScope.launch {
            val personId = initialPerson.id ?: return@launch
            val result = getPersonDetailsUseCase.execute(
                personId = personId,
                language = languageTag,
            )

            if (loadScreenStateFlow.value != result) {
                loadScreenStateFlow.value = result
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialPerson: Person): PersonDetailsViewModel
    }

}