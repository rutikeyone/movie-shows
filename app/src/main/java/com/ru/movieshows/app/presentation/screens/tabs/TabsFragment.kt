package com.ru.movieshows.app.presentation.screens.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.screens.BaseFragment
import com.ru.movieshows.app.presentation.viewmodel.tabs.TabsViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.databinding.FragmentTabsBinding
import com.ru.movieshows.sources.accounts.entities.AuthStateEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TabsFragment : BaseFragment() {

    override val viewModel by viewModels<TabsViewModel>()

    private val tabsTopLevelFragment = setOf(R.id.moviesFragment, R.id.tvsFragment, R.id.profileFragment)

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
        viewModel.authState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach(::handleNotAuthenticatedEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureNavController() = with (binding) {
        val appConfiguration = AppBarConfiguration(tabsTopLevelFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        tabsToolbar.setupWithNavController(navController, appConfiguration)
        tabsToolbar.isTitleCentered = true
        bottomNavigationView.setOnItemReselectedListener(itemReselectedListener)
    }

    private fun handleNotAuthenticatedEvent(state: AuthStateEntity) {
        if(state !is AuthStateEntity.Auth) return
        navController.graph.nodes.forEach { _, value ->
            if(value.id == R.id.profile_tab) {
                navController.popBackStack(R.id.profileFragment, inclusive = false)
            }
        }
    }

}
