package com.ru.movieshows.data.repository

import com.ru.movieshows.data.dto.GenresDto
import com.ru.movieshows.domain.entity.GenreEntity
import com.ru.movieshows.domain.repository.GenresRepository
import com.ru.movieshows.domain.repository.exceptions.GenresException
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(private val genresDto: GenresDto): GenresRepository {
    override suspend fun getGenres(language: String): Result<ArrayList<GenreEntity>> {
        return try {
            val response = genresDto.getGenres(language)
            if(response.isSuccessful && response.body() != null) {
                val genresModel = response.body()!!
                val genresEntity = ArrayList(genresModel.genres.map { it.toEntity() })
                return Result.success(genresEntity)
            } else {
                return Result.failure(GenresException())
            }
        } catch (e: Exception) {
            val genresException = GenresException()
            genresException.initCause(e)
            return Result.failure(genresException)
        }
    }

}