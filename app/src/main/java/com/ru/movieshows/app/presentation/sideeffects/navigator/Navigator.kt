package com.ru.movieshows.app.presentation.sideeffects.navigator

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.app.presentation.sideeffects.ResourceActions

abstract class Navigator {

    open val targetNavigator: ResourceActions<Navigator>
        get() = throw NotImplementedError()

    abstract fun authenticated()

    abstract fun notAuthenticated(isFirstLaunch: Boolean)

    abstract fun navigate(direction: NavDirections)

    abstract fun goBack()

    abstract fun setStartDestination()

    open fun getBottomNavigationView(): BottomNavigationView? {
        throw NotImplementedError()
    }

    open fun setTarget(navigator: Navigator?) {
        throw NotImplementedError()
    }

    open fun clean() {
        throw NotImplementedError()
    }

    open fun getToolbar(): Toolbar? {
        throw NotImplementedError()
    }

    open fun isNestedRoute(): Boolean {
        throw NotImplementedError()
    }

    open fun injectActivity(activity: AppCompatActivity) {
        throw NotImplementedError()
    }
}