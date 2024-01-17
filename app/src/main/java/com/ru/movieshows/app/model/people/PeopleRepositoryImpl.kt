package com.ru.movieshows.app.model.people

import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.people.entities.PersonEntity
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val peopleSource: PeopleSource,
) : PeopleRepository {

    override suspend fun getPersonDetails(
        personId: String,
        language: String,
    ): Either<AppFailure, PersonEntity> {
        return try {
            val result = peopleSource.getPersonDetails(personId, language)
            Either.Right(result)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }

}