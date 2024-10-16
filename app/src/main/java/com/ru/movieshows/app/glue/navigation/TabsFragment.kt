package com.ru.movieshows.app.glue.navigation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.TreeNavigationFragment
import com.ru.movieshows.navigation.presentation.NavigationMode
import com.ru.movieshows.navigation.presentation.NavigationModeHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ru.movieshows.app.*
import com.ru.movieshows.app.databinding.FragmentTabsBinding

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs), TreeNavigationFragment {

    @Inject
    lateinit var destinationsProvider: DestinationsProvider

    @Inject
    lateinit var navigationModeHolder: NavigationModeHolder

    private val binding by viewBinding<FragmentTabsBinding>()

    private val navHost: NavHostFragment
        get() {
            return childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        }

    private val navController: NavController
        get() = navHost.navController

    private val itemReselectedListener = NavigationBarView.OnItemReselectedListener { item ->
        val navTab = destinationsProvider.provideMainTabs().firstOrNull { tab ->
            item.itemId == tab.destinationId
        }

        val fragmentId = navTab?.fragmentId

        fragmentId?.let {
            navController.popBackStack(it, inclusive = false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabsDestinations =
            destinationsProvider.provideMainTabs().map { it.fragmentId }.toSet()
        val appConfiguration = AppBarConfiguration(tabsDestinations)
        val navigationMode = navigationModeHolder.navigationMode

        if (navigationMode is NavigationMode.Tabs) {

            with(binding.bottomNavigationView) {
                setupWithNavController(navController)
                setOnItemReselectedListener(itemReselectedListener)
            }


            with(binding.tabsToolbar) {
                setupWithNavController(navController, appConfiguration)
                isTitleCentered = true
            }
        }
    }

    override fun onResume() {

        with(binding.bottomNavigationView) {
            setOnItemSelectedListener { item ->
                onNavDestinationSelected(
                    item, navController
                )
            }
        }

        super.onResume()
    }

    override fun onPause() {

        with(binding.bottomNavigationView) {
            setOnItemSelectedListener(null)
        }

        super.onPause()
    }

    override fun onDestroyView() {
        with(binding.bottomNavigationView) {
            setOnItemReselectedListener(null)
        }

        super.onDestroyView()
    }

    override fun tabsToolbar(): Toolbar {
        return binding.tabsToolbar
    }

    override fun bottomNavigationView(): BottomNavigationView {
        return binding.bottomNavigationView
    }

}