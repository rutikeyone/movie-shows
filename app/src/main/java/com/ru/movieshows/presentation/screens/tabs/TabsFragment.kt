package com.ru.movieshows.presentation.screens.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTabsBinding
import com.ru.movieshows.dependencies.NavigatorModule
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.viewmodel.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class TabsFragment : BaseFragment() {

    @Inject
    @Named(NavigatorModule.topLevelDestinationsDependency)
    lateinit var tabsTopLevelFragment: Set<Int>

    private val binding by viewBinding<FragmentTabsBinding>()
    private val navHost: NavHostFragment get() = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
    private val navController: NavController get() = navHost.navController

    private val itemReselectedListener = NavigationBarView.OnItemReselectedListener {
        val destination = when(it.itemId) {
            R.id.movies_tab -> R.id.moviesFragment
            R.id.tvs_tab -> R.id.tvsFragment
            R.id.profile_tab -> R.id.profileFragment
            else -> null
        } ?: return@OnItemReselectedListener
        navController.popBackStack(destination, inclusive = false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tabs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureNavController()
    }

    private fun configureNavController() = with (binding) {
        val appConfiguration = AppBarConfiguration(tabsTopLevelFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        tabsToolbar.setupWithNavController(navController, appConfiguration)
        tabsToolbar.isTitleCentered = true
        bottomNavigationView.setOnItemReselectedListener(itemReselectedListener)
    }


}
