package com.ru.movieshows.navigation.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.R
import com.ru.movieshows.navigation.databinding.FragmentTabsBinding
import com.ru.movieshows.navigation.presentation.navigation.NavigationMode
import com.ru.movieshows.navigation.presentation.navigation.NavigationModeHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs) {

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

    private val viewModel by viewModels<TabsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabsDestinations =
            destinationsProvider.provideMainTabs().map { it.destinationId }.toSet()
        val appConfiguration = AppBarConfiguration(tabsDestinations)
        val navigationMode = navigationModeHolder.navigationMode

        if (navigationMode is NavigationMode.Tabs) {
            val menu = binding.bottomNavigationView.menu

            navigationMode.tabs.forEach { tab ->
                val menuItem = menu.add(0, tab.destinationId, Menu.NONE, tab.title)
                menuItem.setIcon(tab.iconRes)
            }

            val graph =
                navController.navInflater.inflate(destinationsProvider.provideTabNavigationGraphId())

            val startDestinationId =
                navigationMode.startTabsDestinationId ?: navigationMode.tabs.first().destinationId

            graph.setStartDestination(startDestinationId)

            navController.graph = graph

            with(binding.bottomNavigationView) {
                setupWithNavController(navController)

                viewModel.selectedItemId?.let {
                    selectedItemId = it
                }

            }


            with(binding.tabsToolbar) {
                setupWithNavController(navController, appConfiguration)
                isTitleCentered = true
            }

            viewModel.navState?.let {
                navController.restoreState(it)
            }

        }
    }

    override fun onResume() {

        with(binding.bottomNavigationView) {
            setOnItemSelectedListener { item ->
                viewModel.selectedItemId = item.itemId

                onNavDestinationSelected(
                    item,
                    navController
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
        viewModel.navState = navController.saveState()
        super.onDestroyView()
    }

}