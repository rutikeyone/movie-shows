package com.ru.movieshows.impl

import android.content.Context
import com.ru.movieshows.core.Resources

class AndroidResources(
    private val applicationContext: Context,
): Resources {

    override fun getString(id: Int): String {
        return applicationContext.getString(id)
    }

    override fun getString(id: Int, vararg placeholders: Any): String {
        return applicationContext.getString(id, placeholders)
    }

}