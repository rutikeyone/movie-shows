package com.ru.movieshows.app.model.settings

interface AppSettingsRepository {

    fun getCurrentSessionId(): String?

    fun setCurrentSessionId(sessionId: String)

    fun cleanCurrentSessionId()

    fun getIsFirstLaunch(): Boolean

    fun setIsFirstLaunch(value: Boolean)
}