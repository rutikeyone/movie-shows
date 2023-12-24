@file:Suppress("UNREACHABLE_CODE")

package com.ru.movieshows.domain.repository

import com.google.gson.Gson
import com.ru.movieshows.data.dto.GenresDto
import com.ru.movieshows.data.repository.GenresRepository
import com.ru.movieshows.domain.entity.GenreEntity
import com.ru.movieshows.domain.utils.AppFailure
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(
    private val genresDto: GenresDto,
    private val gson: Gson,
): GenresRepository {

    override suspend fun getGenres(language: String): Result<ArrayList<GenreEntity>> {
        return try {
            val response = genresDto.getGenres(language)
            val isSuccessful = response.isSuccessful
            val body = response.body()
            val genresModel = body?.genres
            val genresEntity = genresModel?.map { it?.toEntity() }?.let { ArrayList(it) }
            val notNullResult = genresEntity?.filterNotNull()
            if(isSuccessful && notNullResult != null) {
                val result = ArrayList(notNullResult)
                return Result.success(result)
            } else {
                return Result.failure(AppFailure.Pure)
            }
        } catch (e: Exception) {
            val error = handleError(e, gson)
            return Result.failure(error)
        }
    }

}