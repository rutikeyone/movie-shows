package com.ru.movieshows.navigation

import com.ru.movieshows.core.AppRestarter
import javax.inject.Inject

class MainAppRestarter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
): AppRestarter {

    override fun restartApp() {
        globalNavComponentRouter.restart()
    }

}