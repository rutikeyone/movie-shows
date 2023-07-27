package com.ru.movieshows.presentation.screens.tabs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentTabsBinding
import com.ru.movieshows.presentation.MainActivity
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsFragment : BaseFragment(R.layout.fragment_tabs) {
    private val binding by viewBinding<FragmentTabsBinding>()
    private val navHost: NavHostFragment get() = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
    private val navController: NavController get() = navHost.navController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavController()
    }

    private fun setupNavController() {
        val appConfiguration = AppBarConfiguration(tabsTopLevelFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        binding.tabsToolbar.setupWithNavController(navController, appConfiguration)
        binding.tabsToolbar.isTitleCentered = true
    }

    companion object {
        val tabsTopLevelFragment = setOf(R.id.moviesFragment, R.id.tvsFragment, R.id.watchListFragment)
    }
}
