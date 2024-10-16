package com.ru.movieshows.app.glue.navigation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ru.movieshows.app.databinding.ActivityMainBinding
import com.ru.movieshows.core.LoaderOverlay
import com.ru.movieshows.impl.ActivityRequired
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.presentation.NavComponentRouter
import com.ru.movieshows.navigation.presentation.RouterHolder
import com.ru.movieshows.app.*
import com.ru.movieshows.core.presentation.LocaleObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity() : AppCompatActivity(), RouterHolder, LoaderOverlay, LocaleObserver {

    @Inject
    lateinit var navComponentRouterFactory: NavComponentRouter.Factory

    @Inject
    lateinit var destinationsProvider: DestinationsProvider

    @Inject
    lateinit var activityRequiredSet: Set<@JvmSuppressWildcards ActivityRequired>

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navComponentRouter by lazy(LazyThreadSafetyMode.NONE) {
        navComponentRouterFactory.create(R.id.fragmentContainer)
    }

    private val viewModel by viewModels<MainViewModel>()

    private val languageChangedBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == Intent.ACTION_LOCALE_CHANGED) {
                val locale = Locale.getDefault()
                viewModel.notifyLocaleChanges(locale)
            }
        }
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

        val intentFilter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(languageChangedBroadcastReceiver, intentFilter)
    }

    override fun subscribe(): Flow<Locale> {
        return viewModel.localeFlow
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
        navComponentRouter.onDestroy()
        activityRequiredSet.forEach { it.onDestroyed() }

        unregisterReceiver(languageChangedBroadcastReceiver)

        super.onDestroy()
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