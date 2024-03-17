package com.ru.movieshows.navigation.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ru.movieshows.impl.ActivityRequired
import com.ru.movieshows.navigation.DestinationsProvider
import com.ru.movieshows.navigation.R
import com.ru.movieshows.navigation.databinding.ActivityMainBinding
import com.ru.movieshows.navigation.presentation.navigation.NavComponentRouter
import com.ru.movieshows.navigation.presentation.navigation.RouterHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RouterHolder {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(savedInstanceState != null) {
            navComponentRouter.onRestoreInstanceState(savedInstanceState)
        }
        navComponentRouter.onCreate()
        if(savedInstanceState == null) {
            navComponentRouter.switchToStack(destinationsProvider.provideStartDestinationId())
        }
        activityRequiredSet.forEach {
            it.onCreated(this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navComponentRouter.onNavigateUp() || super.onSupportNavigateUp())
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
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
    }

    override fun requireRouter(): NavComponentRouter {
        return navComponentRouter
    }
}