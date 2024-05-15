package com.ru.movieshows.impl

import androidx.fragment.app.FragmentActivity
import com.ru.movieshows.core.ActivityNotCreatedException
import com.ru.movieshows.core.Resources

class AndroidResources(): Resources, ActivityRequired {

    private var currentActivity: FragmentActivity? = null

    override fun getString(id: Int): String {
        val activity = currentActivity ?: throw ActivityNotCreatedException()
        return activity.getString(id)
    }

    override fun getString(id: Int, vararg placeholders: Any): String {
        val activity = currentActivity ?: throw ActivityNotCreatedException()
        return activity.getString(id, placeholders)
    }

    override fun onCreated(activity: FragmentActivity) {
        this.currentActivity = activity
    }

    override fun onDestroyed() {
        this.currentActivity = null
    }

}