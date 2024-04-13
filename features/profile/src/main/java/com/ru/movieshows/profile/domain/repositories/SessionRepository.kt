package com.ru.movieshows.profile.domain.repositories

interface SessionRepository {

    suspend fun clearCurrentSession()

}