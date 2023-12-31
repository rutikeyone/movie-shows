package com.ru.movieshows.presentation.sideeffects.navigator

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
import com.ru.movieshows.databinding.FragmentTabsBinding
import com.ru.movieshows.dependencies.NavigatorModule
import com.ru.movieshows.presentation.screens.tabs.TabsFragment
import javax.inject.Inject
import javax.inject.Named


class NavComponentNavigator @Inject constructor(
    @Named(NavigatorModule.fragmentContainerDependency) private val fragmentContainer: Int,
    @Named(NavigatorModule.appGraphDependency) private val appGraph: Int,
    @Named(NavigatorModule.splashFragmentDependency) private val splashDestination: Int,
    @Named(NavigatorModule.globalSignInDependency) private val toGlobalSignIn: NavDirections,
    @Named(NavigatorModule.globalTabsActionDependency) private val toTabs: NavDirections,
    @Named(NavigatorModule.topLevelDestinationsDependency) val topLevelDestinations: Set<Int>,
) : Navigator(), DefaultLifecycleObserver {
    private lateinit var activity: AppCompatActivity

    private  val rootNavController: NavController get() {
        val fragmentContainer = activity.supportFragmentManager.findFragmentById(fragmentContainer)
        val navHostFragment = fragmentContainer as NavHostFragment
        return navHostFragment.navController
    }

    private var currentNavController: NavController? = null

    private val navHostFragment: NavHostFragment?
        get() {
            val supportFragmentManager = activity.supportFragmentManager
            val fragment = supportFragmentManager.findFragmentById(fragmentContainer)
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
        return topLevelDestinations.any { it == destinationId }
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
        currentNavController?.navigate(direction)
    }

    override fun goBack() {
        currentNavController?.popBackStack()
    }

    override fun authenticated() = rootNavController.navigate(toTabs)

    override fun notAuthenticated() = rootNavController.navigate(toGlobalSignIn)

    override fun setStartDestination() {
        val graph = rootNavController.navInflater.inflate(appGraph)
        graph.setStartDestination(splashDestination)
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
}