package com.ru.movieshows.data.settings.sources

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import com.ru.movieshows.core.AppException
import com.ru.movieshows.impl.ActivityRequired
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception
import javax.inject.Inject

class SharedPreferencesSettingsDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsDataSource, OnSharedPreferenceChangeListener {

    private val currentSessionIdFlow =
        MutableStateFlow<SharedContainer<String?>>(SharedContainer.NoValue())
    private val currentFirstStartingFlow =
        MutableStateFlow<SharedContainer<Boolean>>(SharedContainer.NoValue())

    private val preferences: SharedPreferences = context.getSharedPreferences(
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

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        key: String?,
    ) {
        currentSessionIdFlow.value = SharedContainer.Value(getCurrentSessionId())
        currentFirstStartingFlow.value = SharedContainer.Value(getFirstLaunch())
    }

    override fun listenCurrentSessionId(): Flow<SharedContainer<String?>> {
        return currentSessionIdFlow
    }

    override fun getFirstLaunch(): Boolean {
        return preferences.getBoolean(FIRST_STARTING, true)
    }

    override fun setFirstLaunch(value: Boolean) {
        preferences.edit {
            putBoolean(FIRST_STARTING, value)
        }
    }

    override fun listenFirstLaunch(): Flow<SharedContainer<Boolean>> {
        return currentFirstStartingFlow
    }

    private companion object {
        const val PREFERENCES_NAME = "preferences"
        private const val SESSION_ID = "session_id"
        private const val FIRST_STARTING = "first_starting"
    }

}