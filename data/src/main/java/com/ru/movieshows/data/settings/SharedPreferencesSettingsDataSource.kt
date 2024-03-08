package com.ru.movieshows.data.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SharedPreferencesSettingsDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsDataSource, OnSharedPreferenceChangeListener {

    private val currentSessionIdFlow = MutableStateFlow<String?>(null)
    private val currentFirstStartingFlow = MutableStateFlow<Boolean?>(null)

    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE,
    )

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun getCurrentSessionId(): String? {
        return preferences.getString(SESSION_ID, null)
    }

    override fun setCurrentSessionId(sessionId: String?) {
        preferences.edit {
            if (sessionId == null) {
                remove(SESSION_ID)
            } else {
                putString(SESSION_ID, sessionId)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentSessionIdFlow.value = getCurrentSessionId()
        currentFirstStartingFlow.value = getFirstStarting()
    }

    override fun listenCurrentSessionId(): Flow<String?> {
        return currentSessionIdFlow
    }

    override fun getFirstStarting(): Boolean {
        return preferences.getBoolean(FIRST_STARTING, true)
    }

    override fun setFirstStarting(value: Boolean) {
        preferences.edit {
            putBoolean(FIRST_STARTING, value)
        }
    }

    override fun listenFirstStarting(): Flow<Boolean?> {
        return currentFirstStartingFlow
    }

    private companion object {
        const val PREFERENCES_NAME = "preferences"
        private const val SESSION_ID = "session_id"
        private const val FIRST_STARTING = "first_starting"
    }

}