package com.ru.movieshows.signin.domain.repositories

interface AuthSessionIdRepository {

    suspend fun setCurrentSessionId(sessionId: String?)

    suspend fun getCurrentSessionId(): String?

}