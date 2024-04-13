package com.ru.movieshows.app.glue.profile

import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.profile.presentation.ProfileRouter
import javax.inject.Inject

class AdapterProfileRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : ProfileRouter {

    override fun launchMain() {
        globalNavComponentRouter.startTabs()
    }

}