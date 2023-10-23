package com.ru.movieshows.data.repository

interface AppSettingsRepository {

    fun getCurrentSessionId(): String?

    fun setCurrentSessionId(sessionId: String)

    fun cleanCurrentSessionId()
}