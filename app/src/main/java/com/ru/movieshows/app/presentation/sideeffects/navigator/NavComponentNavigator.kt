package com.ru.movieshows.app.presentation.sideeffects.navigator

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.app.presentation.screens.tabs.TabsFragment
import com.ru.movieshows.databinding.FragmentTabsBinding
import javax.inject.Inject

data class NavigationOptions(
    val fragmentContainer: Int,
    val appGraph: Int,
    val splashDestination: Int,
    val toSignIn: NavDirections,
    val toTabs: NavDirections,
    val topLevelDestinations: Set<Int>,
    val tabFragmentId: Int,
)

class NavComponentNavigator @Inject constructor(
    private val options: NavigationOptions
) : Navigator(), DefaultLifecycleObserver {

    private lateinit var activity: AppCompatActivity

    private val rootNavController: NavController get() {
        val fragmentContainer = activity.supportFragmentManager.findFragmentById(options.fragmentContainer)
        val navHostFragment = fragmentContainer as NavHostFragment
        return navHostFragment.navController
    }

    private var currentNavController: NavController? = null

    private val navHostFragment: NavHostFragment?
        get() {
            val supportFragmentManager = activity.supportFragmentManager
            val fragment = supportFragmentManager.findFragmentById(options.fragmentContainer)
            return fragment as? NavHostFragment
        }

    private val currentFragment: Fragment?
        get() {
            val navHostFragment = navHostFragment
            return navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
        }

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentResumed(fragmentManager: FragmentManager, fragment: Fragment) {
            super.onFragmentResumed(fragmentManager, fragment)
            if(fragment is NavHostFragment) return
            onNavControllerActivated(fragment.findNavController())
        }

    }

    private fun nestedRoute(): Boolean {
        val destinationId = currentNavController?.currentDestination?.id
        return options.topLevelDestinations.any { it == destinationId }
    }

    private fun popBackStack(): Boolean {
        return if(currentNavController != null) {
            currentNavController!!.popBackStack()
        } else {
            false
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(nestedRoute()) {
                activity.finish()
            } else if(!popBackStack()) {
                isEnabled = false
                activity.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.currentNavController == navController || navController == rootNavController) return
        this.currentNavController = navController
    }

    override fun injectActivity(activity: AppCompatActivity) {
        this.activity = activity
        activity.lifecycle.removeObserver(this);
        activity.lifecycle.addObserver(this);
    }

    private fun configureNavigator() {
        onNavControllerActivated(rootNavController)
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        configureNavigator()
    }

    override fun onStart(owner: LifecycleOwner) {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onStop(owner)
    }

    override fun navigate(direction: NavDirections) {
        if(currentNavController != null) {
            currentNavController?.navigate(direction)
        } else {
            rootNavController.navigate(direction)
        }
    }

    override fun goBack() {
        currentNavController?.popBackStack()
    }

    override fun authenticated() = rootNavController.navigate(options.toTabs)

    override fun notAuthenticated(isFirstLaunch: Boolean) {
        if(isFirstLaunch) {
            rootNavController.navigate(options.toSignIn)
        } else {
            rootNavController.navigate(options.toTabs)
        }
    }

    override fun setStartDestination() {
        val graph = rootNavController.navInflater.inflate(options.appGraph)
        graph.setStartDestination(options.splashDestination)
        rootNavController.graph = graph
        currentNavController = rootNavController
    }

    override fun getToolbar(): Toolbar? {
        val tabsFragment = currentFragment as? TabsFragment
        val view = tabsFragment?.view ?: return null
        val binding = FragmentTabsBinding.bind(view)
        return binding.tabsToolbar
    }

    override fun getBottomNavigationView(): BottomNavigationView? {
        val tabsFragment = currentFragment as? TabsFragment
        val view = tabsFragment?.view ?: return null
        val binding = FragmentTabsBinding.bind(view)
        return binding.bottomNavigationView
    }

    override fun isNestedRoute(): Boolean {
        val navController = currentNavController ?: rootNavController
        return navController.graph.id == options.tabFragmentId
    }
    
}