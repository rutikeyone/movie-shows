package com.ru.movieshows.data.settings.sources

import kotlinx.coroutines.flow.Flow

sealed class SharedContainer<T> {

    class NoValue<T> : SharedContainer<T>()

    data class Value<T>(
        val value: T,
    ) : SharedContainer<T>()

}

interface SettingsDataSource {

    fun getCurrentSessionId(): String?

    fun setCurrentSessionId(sessionId: String?)

    fun listenCurrentSessionId(): Flow<SharedContainer<String?>>

    fun getFirstLaunch(): Boolean

    fun setFirstLaunch(value: Boolean)

    fun listenFirstLaunch(): Flow<SharedContainer<Boolean>>

}