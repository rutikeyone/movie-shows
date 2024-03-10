package com.ru.movieshows.data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {

    fun getCurrentSessionId(): String?

    fun setCurrentSessionId(sessionId: String?)

    fun listenCurrentSessionId(): Flow<String?>

    fun getFirstStarting(): Boolean

    fun setFirstStarting(value: Boolean)

    fun listenFirstStarting(): Flow<Boolean?>

}