package com.ru.movieshows.domain.repository

import android.content.Context
import com.ru.movieshows.data.repository.AppSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AppSettingsRepositoryImpl @Inject constructor(
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

    companion object {
        private const val SETTINGS = "settings"
        private const val SESSION_ID = "session_id"
    }
}