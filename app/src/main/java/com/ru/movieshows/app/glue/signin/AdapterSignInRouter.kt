package com.ru.movieshows.app.glue.signin

import com.ru.movieshows.app.R
import com.ru.movieshows.navigation.GlobalNavComponentRouter
import com.ru.movieshows.navigation.presentation.navigation.NavigationMode
import com.ru.movieshows.signin.presentation.SignInRouter
import javax.inject.Inject

class AdapterSignInRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : SignInRouter {

    override fun launchMain() {
        val navigationMode = globalNavComponentRouter.navigationMode
        when (navigationMode) {
            NavigationMode.Stack -> globalNavComponentRouter.startTabs()
            is NavigationMode.Tabs -> globalNavComponentRouter.pop(R.id.tabsFragment)
        }
    }

    override fun isTabsNavigationMode(): Boolean {
        val navigationMode = globalNavComponentRouter.navigationMode
        return navigationMode is NavigationMode.Tabs
    }

}