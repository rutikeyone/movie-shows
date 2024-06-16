package com.ru.movieshows.impl

import androidx.fragment.app.FragmentActivity
import com.ru.movieshows.core.ActivityNotCreatedException
import com.ru.movieshows.core.LoaderOverlay

class AndroidLoaderOverlay() : LoaderOverlay,  ActivityRequired {

    private var currentActivity: FragmentActivity? = null

    override fun onCreated(activity: FragmentActivity) {
        this.currentActivity = activity
    }

    override fun onDestroyed() {
        this.currentActivity = null
    }

    override fun showLoader() {
        val activity = currentActivity ?: throw ActivityNotCreatedException()
        if(activity is LoaderOverlay) {
            activity.showLoader()
        }
    }

    override fun hideLoader() {
        val activity = currentActivity ?: throw ActivityNotCreatedException()
        if(activity is LoaderOverlay) {
            activity.hideLoader()
        }
    }

}