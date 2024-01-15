package com.ru.movieshows.app.model.settings

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SharedPreferencesAppSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppSettingsRepository {
    private val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    override fun getCurrentSessionId(): String? = sharedPreferences.getString(SESSION_ID, null)

    override fun setCurrentSessionId(sessionId: String) {
        sharedPreferences
            .edit()
            .putString(SESSION_ID, sessionId)
            .apply()

    }

    override fun cleanCurrentSessionId() {
        sharedPreferences
            .edit()
            .remove(SESSION_ID)
            .apply()
    }

    override fun getIsFirstLaunch(): Boolean {
        val isFirstLaunch = sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true)
        if(isFirstLaunch) {
            setIsFirstLaunch(false)
        }
        return isFirstLaunch
    }

    override fun setIsFirstLaunch(value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(IS_FIRST_LAUNCH, value)
            .apply()
    }

    companion object {
        private const val SETTINGS = "settings"
        private const val SESSION_ID = "session_id"
        private const val IS_FIRST_LAUNCH = "is_first_launch"
    }
}