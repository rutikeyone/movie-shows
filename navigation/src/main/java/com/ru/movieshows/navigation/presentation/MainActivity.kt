package com.ru.movieshows.navigation.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ru.movieshows.core.LoaderOverlay
import com.ru.movieshows.core.presentation.observeStateOn
import com.ru.movieshows.impl.ActivityRequired
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.R
import com.ru.movieshows.navigation.databinding.ActivityMainBinding
import com.ru.movieshows.navigation.presentation.navigation.NavComponentRouter
import com.ru.movieshows.navigation.presentation.navigation.RouterHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RouterHolder, LoaderOverlay {

    @Inject
    lateinit var navComponentRouterFactory: NavComponentRouter.Factory

    @Inject
    lateinit var destinationsProvider: DestinationsProvider

    @Inject
    lateinit var activityRequiredSet: Set<@JvmSuppressWildcards ActivityRequired>

    private val viewModel by viewModels<MainViewModel>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navComponentRouter by lazy(LazyThreadSafetyMode.NONE) {
        navComponentRouterFactory.create(R.id.fragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            navComponentRouter.onRestoreInstanceState(savedInstanceState)
        }
        navComponentRouter.onCreate()
        if (savedInstanceState == null) {
            navComponentRouter.switchToStack(destinationsProvider.provideStartDestinationId())
        }
        activityRequiredSet.forEach {
            it.onCreated(this)
        }
        onBackPressedDispatcher.addCallback(navComponentRouter.onBackPressedCallback)
        lifecycle.addObserver(viewModel)

    }

    override fun onSupportNavigateUp(): Boolean {
        return (navComponentRouter.onNavigateUp() || super.onSupportNavigateUp())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navComponentRouter.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        activityRequiredSet.forEach { it.onStarted() }
    }

    override fun onStop() {
        super.onStop()
        activityRequiredSet.forEach { it.onStopped() }
    }

    override fun onDestroy() {
        super.onDestroy()
        navComponentRouter.onDestroy()
        activityRequiredSet.forEach { it.onDestroyed() }
        lifecycle.removeObserver(viewModel)
    }

    override fun requireRouter(): NavComponentRouter {
        return navComponentRouter
    }

    override fun showLoader() {
        with(binding) {
            loaderOverlay.progressOverlay.isVisible = true
        }
    }

    override fun hideLoader() {
        with(binding) {
            loaderOverlay.progressOverlay.isVisible = false
        }
    }

}