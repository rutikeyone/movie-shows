package com.ru.movieshows.season.domain

import com.ru.movieshows.season.domain.repositories.PeopleRepository
import javax.inject.Inject

class GetPersonImagesUseCase @Inject constructor(
    private val peopleRepository: PeopleRepository,
) {

    suspend fun execute(personId: String): List<String>? {
        return peopleRepository.getPersonImages(personId)
    }

}