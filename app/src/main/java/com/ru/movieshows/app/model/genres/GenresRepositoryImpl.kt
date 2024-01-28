package com.ru.movieshows.app.model.genres

import arrow.core.Either
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.sources.genres.entities.GenreEntity
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(
    private val genresSource: GenresSource,
): GenresRepository {

    override suspend fun getGenres(language: String): Either<AppFailure, ArrayList<GenreEntity>> {
        return try {
            val genres = genresSource.getGenres(language)
            Either.Right(genres)
        } catch (e: AppFailure) {
            Either.Left(e)
        } catch (e: Exception) {
            Either.Left(AppFailure.Empty)
        }
    }
}