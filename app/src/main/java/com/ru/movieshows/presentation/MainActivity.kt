package com.ru.movieshows.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ru.movieshows.R
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.sign_in.SignInFragmentDirections
import com.ru.movieshows.presentation.screens.splash.SplashFragmentDirections
import com.ru.movieshows.presentation.screens.tabs.TabsFragment
import com.ru.movieshows.presentation.screens.tabs.TabsFragmentDirections
import com.ru.movieshows.presentation.viewmodel.main.AuthState
import com.ru.movieshows.presentation.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.authFailureState.observe(this, ::authFailureStateChanged)
    }

    private fun authFailureStateChanged(failure: AppFailure?) {
        if(failure == null) return
        val message = when (failure) {
            AppFailure.Pure -> R.string.there_was_a_problem_with_authorization
            AppFailure.Connection -> R.string.connect_to_the_internet
            is AppFailure.Message -> failure.value
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun stateChanged(state: AuthState) {
        when (state) {
            is AuthState.Authenticated -> navigateByAuthenticatedState()
            AuthState.NotAuthenticated -> navigateByNotAuthenticatedState()
            else -> setStartDestination()
        }
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

    private fun setStartDestination() {
        val graph = rootNavController.navInflater.inflate(getAppNavigationGraphId())
        graph.setStartDestination(getSplashDestination())
        rootNavController.graph = graph
        _currentNavController = rootNavController
    }

    private fun navigateByNotAuthenticatedState() {
        when(rootNavController.currentDestination?.id) {
            R.id.splashFragment -> rootNavController.navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
            R.id.tabsFragment -> rootNavController.navigate(TabsFragmentDirections.actionTabsFragmentToSignInFragment4())
        }
    }

    private fun navigateByAuthenticatedState() {
        when(rootNavController.currentDestination?.id) {
            R.id.splashFragment -> rootNavController.navigate(SplashFragmentDirections.actionSplashFragmentToTabsFragment())
            R.id.signInFragment -> rootNavController.navigate(SignInFragmentDirections.actionSignInFragmentToTabsFragment3())
        }
    }

    private fun getAppNavigationGraphId(): Int = R.navigation.app_graph

    private fun getSplashDestination(): Int = R.id.splashFragment

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onDestroy()
    }

}