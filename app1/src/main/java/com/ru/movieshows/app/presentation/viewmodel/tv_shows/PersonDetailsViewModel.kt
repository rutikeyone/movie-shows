package com.ru.movieshows.app.presentation.viewmodel.tv_shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.app.model.people.PeopleRepository
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.people.entities.PersonEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class PersonDetailsViewModel @AssistedInject constructor(
    @Assisted private val initialPerson: PersonEntity,
    private val peopleRepository: PeopleRepository,
): BaseViewModel() {

    private var _person = MutableLiveData<PersonEntity?>(null)
    val person = _person.share()

    init {
        _person.value = initialPerson
    }

    fun update() = viewModelScope.launch {
        val personId = initialPerson.id ?: return@launch
        val language = languageTag
        val getPersonDetailsResult = peopleRepository.getPersonDetails(personId, language)
        val result = getPersonDetailsResult.getOrNull()
        if(result != null && _person.value != result) {
            _person.value = result
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialPerson: PersonEntity): PersonDetailsViewModel
    }

}