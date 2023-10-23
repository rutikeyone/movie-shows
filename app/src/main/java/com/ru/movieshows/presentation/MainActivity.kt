package com.ru.movieshows.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ru.movieshows.R
import com.ru.movieshows.presentation.screens.tabs.TabsFragment
import com.ru.movieshows.presentation.viewmodel.main.AuthState
import com.ru.movieshows.presentation.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates.notNull

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val rootNavController get() = rootNavController()

    private var _currentNavController: NavController? = null

    private val viewModel by viewModels<MainViewModel>()


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = executeOnHandleBack(this)
    }

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(fragmentManager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fragmentManager, fragment, savedInstanceState)
            if(fragment is NavHostFragment) return
            onNavControllerActivated(fragment.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        viewModel.state.observe(this, ::stateChanged)
    }

    private fun stateChanged(state: AuthState) {
        var route by notNull<Int>()

        route = when (state) {
            is AuthState.Authenticated -> R.id.action_global_tabsFragment
            AuthState.NotAuthenticated -> R.id.action_global_signInFragment
            else -> R.id.action_global_splashFragment
        }
        rootNavController.navigate(route)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onDestroy()
    }

    private fun isRootNestedRoute(): Boolean {
        val destinationId = _currentNavController?.currentDestination?.id
        return TabsFragment.tabsTopLevelFragment.any { it == destinationId }
    }

    private fun popToBackInCurrentNavControllerIfCan(): Boolean {
        return _currentNavController != null && _currentNavController?.popBackStack() == true
    }

    private fun executeOnHandleBack(onBackPressedCallback: OnBackPressedCallback) {
        when (isRootNestedRoute()) {
            true -> finish()
            false -> {
                if(popToBackInCurrentNavControllerIfCan()) return
                onBackPressedCallback.isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun rootNavController(): NavController {
        val fragmentContainer = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navHostFragment = fragmentContainer as NavHostFragment
        return navHostFragment.navController
    }

    private fun setupNavigation() {
        onNavControllerActivated(rootNavController)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    private fun onNavControllerActivated(navController: NavController) {
        if(this._currentNavController == navController || navController == rootNavController) return
        this._currentNavController = navController
    }
}