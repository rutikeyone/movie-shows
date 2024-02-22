package com.ru.movieshows.app.di

import android.content.Context
import com.ru.movieshows.AppGraphDirections
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.sideeffects.navigator.IntermediateNavigator
import com.ru.movieshows.app.presentation.sideeffects.navigator.NavComponentNavigator
import com.ru.movieshows.app.presentation.sideeffects.navigator.NavigationOptions
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.resources.ResourcesImpl
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.presentation.sideeffects.toast.ToastsImpl
import com.ru.movieshows.app.utils.dispatcher.Dispatcher
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NavigatorWrapper

@Module
@InstallIn(SingletonComponent::class)
interface SideEffectsModule {

    @Binds
    fun bindToast(toasts: ToastsImpl): Toasts

    @Binds
    fun bindResources(resourcesImpl: ResourcesImpl): Resources

    @Binds
    @NavigatorWrapper
    @Singleton
    fun bindIntermediateNavigator(navigator: IntermediateNavigator): Navigator

    @Binds
    fun bindNavigator(navigator: NavComponentNavigator): Navigator

    companion object {
        const val INTERMEDIATE_NAVIGATOR_DEPENDENCY = "intermediateNavigatorDependency"
    }

}