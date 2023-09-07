package com.ru.movieshows.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ru.movieshows.R
import com.ru.movieshows.presentation.screens.tabs.TabsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val rootNavController get() = rootNavController()

    private var _currentNavController: NavController? = null

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
                val rootCurrentDestinationId = rootNavController.currentDestination?.id
                val currentDestinationId = _currentNavController?.currentDestination?.id
                val isYoutubePlayerDestination = rootCurrentDestinationId ==  R.id.youtubeVideoPlayerFragment2
                if(isYoutubePlayerDestination) {
                    rootNavController.popBackStack()
                    return
                }
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