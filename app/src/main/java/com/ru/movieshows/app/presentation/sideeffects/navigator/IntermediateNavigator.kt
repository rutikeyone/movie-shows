package com.ru.movieshows.app.presentation.sideeffects.navigator

import androidx.navigation.NavDirections
import com.ru.movieshows.app.di.NavigatorWrapper
import com.ru.movieshows.app.presentation.sideeffects.ResourceActions
import javax.inject.Inject

class IntermediateNavigator @Inject constructor() : Navigator() {

    override val targetNavigator: ResourceActions<Navigator> =  ResourceActions()

    override fun setTarget(navigator: Navigator?) {
        targetNavigator.resource = navigator
    }

    override fun clean() {
        targetNavigator.clear()
    }

    override fun authenticated() = targetNavigator { navigator ->
        navigator.authenticated()
    }

    override fun notAuthenticated(isFirstLaunch: Boolean) = targetNavigator { navigator ->
        navigator.notAuthenticated(isFirstLaunch)
    }

    override fun navigate(direction: NavDirections) = targetNavigator { navigator ->
        navigator.navigate(direction)
    }

    override fun goBack() = targetNavigator { navigator ->
          navigator.goBack()
    }

    override fun setStartDestination() = targetNavigator { navigator ->
        navigator.setStartDestination()
    }

    override fun isNestedRoute(): Boolean {
        return targetNavigator.resource?.isNestedRoute() ?: false
    }

}