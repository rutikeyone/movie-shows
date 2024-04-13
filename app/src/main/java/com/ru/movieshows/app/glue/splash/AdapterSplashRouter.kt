package com.ru.movieshows.app.glue.splash

import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.splash.presentation.SplashRouter
import javax.inject.Inject

class AdapterSplashRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : SplashRouter {

    override fun launchMain() {
        globalNavComponentRouter.startTabs()
    }

    override fun launchSignIn() {
        globalNavComponentRouter.startAuth()
    }
}