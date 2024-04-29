package com.ru.movieshows.navigation

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.impl.ActivityRequired
import com.ru.movieshows.navigation.domain.exceptions.ActivityNotCreatedException
import com.ru.movieshows.navigation.presentation.navigation.NavComponentRouter
import com.ru.movieshows.navigation.presentation.navigation.NavigationMode
import com.ru.movieshows.navigation.presentation.navigation.RouterHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalNavComponentRouter @Inject constructor(
    private val destinationsProvider: DestinationsProvider,
) : ActivityRequired {

    private var activity: FragmentActivity? = null
    private var started = false
    private var completelyDestroyed = true
    private val commands = mutableListOf<() -> Unit>()

    private val onBackPressHandlers = LinkedHashSet<() -> Boolean>()

    val navigationMode: NavigationMode
        get() {
            return this.activity?.let {
                (it as RouterHolder).requireRouter().navigationModeHolder.navigationMode
            } ?: throw ActivityNotCreatedException()
        }

    override fun onCreated(activity: FragmentActivity) {
        this.completelyDestroyed = false
        this.activity = activity
        setupBackHandlers()
    }

    override fun onStarted() {
        started = true
        commands.forEach { it() }
        commands.clear()
    }

    override fun onStopped() {
        started = false
    }

    override fun onDestroyed() {
        if (activity?.isFinishing == true) {
            commands.clear()
            completelyDestroyed = true
            activity = null
        }
    }

    fun registerBackHandler(scope: CoroutineScope, handler: () -> Boolean) {
        scope.launch {
            suspendCancellableCoroutine { continuation ->
                onBackPressHandlers.add(handler)
                continuation.invokeOnCancellation {
                    onBackPressHandlers.remove(handler)
                }
            }
        }
    }

    fun pop(tabsFragment: Int) = invoke {
        requireRealRouter().pop()
    }

    fun getToolbar(): Toolbar? {
        val routerHolder = activity as? RouterHolder
        return routerHolder?.requireRouter()?.getToolbar()
    }

    fun getBottomNavigationView(): BottomNavigationView? {
        val routerHolder = activity as? RouterHolder
        return routerHolder?.requireRouter()?.getBottomNavigationView()
    }

    fun restart() = invoke {
        launch(
            destinationId = destinationsProvider.provideStartAuthDestinationId(),
            args = null,
            root = true,
        )
    }

    fun launch(
        @IdRes destinationId: Int,
        args: java.io.Serializable? = null,
        root: Boolean = false,
    ) = invoke {
        requireRealRouter().launch(destinationId, args, root)
    }

    fun pop(
        @IdRes destinationId: Int,
        inclusive: Boolean,
        root: Boolean = false,
    ) = invoke {
        requireRealRouter().pop(destinationId, inclusive, root)
    }

    fun startAuth() = invoke {
        requireRealRouter().switchToStack(destinationsProvider.provideStartAuthDestinationId())
    }

    fun startTabs(startTabDestinationId: Int? = null) = invoke {
        requireRealRouter().switchToTabs(
            destinationsProvider.provideMainTabs(),
            startTabDestinationId
        )
    }

    private fun setupBackHandlers() {
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (requireRealRouter().isDialog()) {
                        processAsUsual()
                        return
                    }
                    onBackPressHandlers.reversed().forEach { handler ->
                        if (handler.invoke()) {
                            return
                        }
                    }
                    processAsUsual()
                }

                private fun processAsUsual() {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            })
    }

    private fun invoke(command: () -> Unit) {
        if (started) {
            command()
        } else if (!completelyDestroyed) {
            commands.add(command)
        }
    }

    private fun requireRealRouter(): NavComponentRouter {
        return (activity as RouterHolder).requireRouter()
    }

    private fun requireActivity(): FragmentActivity {
        return activity!!
    }

}