package com.ru.movieshows.navigation.presentation.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.core.presentation.ARG_SCREEN
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.databinding.FragmentTabsBinding
import com.ru.movieshows.navigation.presentation.TabsFragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.regex.Pattern

class NavComponentRouter @AssistedInject constructor(
    @Assisted @IdRes private val fragmentContainerId: Int,
    private val destinationsProvider: DestinationsProvider,
    private val navOptions: NavOptions,
    private val activity: FragmentActivity,
    val navigationModeHolder: NavigationModeHolder,
) {

    private var currentStartDestination = 0
    private var fragmentDialogs: Int = 0

    private var navController: NavController? = null

    private val currentNavController: NavController
        get() {
            return when (navigationModeHolder.navigationMode) {
                NavigationMode.Stack -> getRootNavController()
                is NavigationMode.Tabs -> navController ?: getRootNavController()
            }
        }

    private val destinationListeners = mutableSetOf<() -> Unit>()

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            super.onFragmentStarted(fm, f)
            if (f is DialogFragment) fragmentDialogs++
            if (f is NavHostFragment || f is TabsFragment) return
            val currentNavController = f.findNavController()

            onNavControllerActivated(currentNavController)
            destinationListeners.forEach { it() }
        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            if (f is DialogFragment) fragmentDialogs--
        }

    }

    private val destinationListener =
        NavController.OnDestinationChangedListener { _, destination, arguments ->
            val appCompactActivity =
                activity as? AppCompatActivity ?: return@OnDestinationChangedListener
            val title = prepareTitle(destination.label, arguments)
            if (title.isNotBlank()) {
                appCompactActivity.supportActionBar?.title = title
            }
            appCompactActivity.supportActionBar?.setDisplayHomeAsUpEnabled(
                !isStartDestination(
                    destination
                )
            )
        }

    private val isMainTabDestination: Boolean
        get() {
            val destinationId = currentNavController.currentDestination?.id
            val mainTabDestinations =
                destinationsProvider.provideMainTabs().map { it.destinationId }
            return mainTabDestinations.any { it == destinationId }
        }

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isMainTabDestination) {
                activity.finish()
            } else if (!pop()) {
                isEnabled = false
                activity.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private val navHostFragment: NavHostFragment?
        get() {
            val supportFragmentManager = activity.supportFragmentManager
            val fragment = supportFragmentManager.findFragmentById(fragmentContainerId)
            return fragment as? NavHostFragment
        }

    private val currentFragment: Fragment?
        get() {
            val navHostFragment = navHostFragment
            return navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
        }

    fun onCreate() {
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    fun onDestroy() {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        destinationListeners.clear()
    }

    fun onNavigateUp(): Boolean {
        pop()
        return true
    }

    fun onSaveInstanceState(bundle: Bundle) {
        bundle.putInt(KEY_START_DESTINATION, currentStartDestination)
        bundle.putSerializable(KEY_NAV_MODE, navigationModeHolder.navigationMode)
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        currentStartDestination = bundle.getInt(KEY_START_DESTINATION, 0)
        @Suppress("DEPRECATION")
        navigationModeHolder.navigationMode =
            bundle.getSerializable(KEY_NAV_MODE) as? NavigationMode
                ?: throw IllegalStateException("No state to be restored")
        restoreRoot()
    }

    fun switchToStack(@IdRes initialDestinationId: Int) {
        navigationModeHolder.navigationMode = NavigationMode.Stack
        switchRoot(initialDestinationId)
        currentStartDestination = initialDestinationId
    }

    fun switchToTabs(rootDestinations: List<NavTab>, startTabDestinationId: Int?) {
        navigationModeHolder.navigationMode = NavigationMode.Tabs(
            ArrayList(rootDestinations),
            startTabDestinationId,
        )
        switchRoot(destinationsProvider.provideTabsDestinationId())
        currentStartDestination = destinationsProvider.provideTabsDestinationId()
    }

    fun launch(
        @IdRes destinationId: Int,
        args: java.io.Serializable? = null,
        root: Boolean,
        options: NavOptions?,
        ) {
        val navController = if (root) getRootNavController() else currentNavController
        if (args == null) {
            navController.navigate(
                resId = destinationId,
                args = null,
                navOptions = options ?: navOptions,
            )
        } else {
            navController.navigate(
                resId = destinationId,
                args = Bundle().apply {
                    putSerializable(ARG_SCREEN, args)
                },
                navOptions = options ?: navOptions,
            )
        }
    }

    fun pop(
        @IdRes destinationId: Int,
        inclusive: Boolean = true,
        root: Boolean,
    ) {
        val navController = if (root) getRootNavController() else currentNavController
        navController.popBackStack(destinationId, inclusive)
    }


    fun pop(): Boolean {
        return currentNavController.popBackStack()
    }

    internal fun addDestinationListener(listener: () -> Unit) {
        destinationListeners.add(listener)
    }

    @SuppressLint("RestrictedApi")
    internal fun hasDestinationId(id: Int): Boolean {
        return currentNavController.findDestination(id) != null
    }

    internal fun isDialog(): Boolean {
        return fragmentDialogs > 0
    }

    private fun switchRoot(@IdRes rootDestinationId: Int) {
        if (currentStartDestination == 0) {
            restoreRoot()
        } else {
            getRootNavController().navigate(
                resId = rootDestinationId,
                args = null,
                navOptions {
                    popUpTo(currentStartDestination) {
                        inclusive = true
                    }
                }
            )
        }
    }

    private fun restoreRoot() {
        val graph =
            getRootNavController().navInflater.inflate(destinationsProvider.provideNavigationGraphId())
        graph.setStartDestination(destinationsProvider.provideStartDestinationId())
        getRootNavController().graph = graph
    }

    private fun getRootNavController(): NavController {
        val fragmentManager = activity.supportFragmentManager
        val navHost = fragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
        return navHost.navController
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startTabsDestinations = destinationsProvider.provideMainTabs().map { it.destinationId }
        val startDestinations = startTabsDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {
        if (label == null) return ""
        val title = StringBuffer()
        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
        val matcher = fillInPattern.matcher(label)
        while (matcher.find()) {
            val argName = matcher.group(1)
            if (arguments != null && arguments.containsKey(argName)) {
                matcher.appendReplacement(title, "")
                title.append(arguments[argName].toString())
            } else {
                throw IllegalArgumentException(
                    "Could not find $argName in $arguments to fill label $label"
                )
            }
        }
        matcher.appendTail(title)
        return title.toString()
    }

    fun getToolbar(): Toolbar? {
        val tabsFragment = currentFragment as? TabsFragment
        val view = tabsFragment?.view ?: return null
        val binding = FragmentTabsBinding.bind(view)
        return binding.tabsToolbar
    }

    fun getBottomNavigationView(): BottomNavigationView? {
        val tabsFragment = currentFragment as? TabsFragment
        val view = tabsFragment?.view ?: return null
        val binding = FragmentTabsBinding.bind(view)
        return binding.bottomNavigationView
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @IdRes fragmentContainerId: Int,
        ): NavComponentRouter
    }

    private companion object {
        const val KEY_START_DESTINATION = "startDestination"
        const val KEY_NAV_MODE = "navMode"
    }
}