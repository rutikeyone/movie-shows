package com.ru.movieshows.data.settings

import com.ru.movieshows.data.SettingsDataRepository
import com.ru.movieshows.data.settings.sources.SettingsDataSource
import com.ru.movieshows.data.settings.sources.SharedContainer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class SettingsDataRepositoryImpl @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
) : SettingsDataRepository {

    private var _firstLaunchAfterInit by Delegates.notNull<Boolean>()

    init {
        _firstLaunchAfterInit = settingsDataSource.getFirstLaunch()
    }

    override fun getCurrentSessionId(): String? {
        return settingsDataSource.getCurrentSessionId()
    }

    override fun setCurrentSessionId(sessionId: String?) {
        settingsDataSource.setCurrentSessionId(sessionId)
    }

    override fun listenCurrentSessionId(): Flow<SharedContainer<String?>> {
        return settingsDataSource.listenCurrentSessionId()
    }

    override fun getFirstLaunchAfterInit(): Boolean {
        return _firstLaunchAfterInit
    }

    override fun setFirstLaunch(value: Boolean) {
        return settingsDataSource.setFirstLaunch(value)
    }

    override fun getFirstLaunch(): Boolean {
        return settingsDataSource.getFirstLaunch()
    }

    override fun listenFirstLaunch(): Flow<SharedContainer<Boolean>> {
        return settingsDataSource.listenFirstLaunch()
    }

}