package com.ru.movieshows.data

import com.ru.movieshows.data.settings.sources.SharedContainer
import kotlinx.coroutines.flow.Flow

interface SettingsDataRepository {

    fun getCurrentSessionId(): String?

    fun setCurrentSessionId(sessionId: String?)

    fun listenCurrentSessionId(): Flow<SharedContainer<String?>>

    fun getFirstLaunchAfterInit(): Boolean

    fun getFirstLaunch(): Boolean

    fun setFirstLaunch(value: Boolean)

    fun listenFirstLaunch(): Flow<SharedContainer<Boolean>>

}