package com.ru.movieshows.app.presentation.sideeffects.resources

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourcesImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) : Resources {

    override fun getString(resId: Int, vararg args: Any): String {
        return applicationContext.getString(resId, *args)
    }

}