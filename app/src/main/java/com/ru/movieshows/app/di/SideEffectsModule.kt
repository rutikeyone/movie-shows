package com.ru.movieshows.app.di

import com.ru.movieshows.app.presentation.sideeffects.navigator.IntermediateNavigator
import com.ru.movieshows.app.presentation.sideeffects.navigator.NavComponentNavigator
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.resources.ResourcesImpl
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.presentation.sideeffects.toast.ToastsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
abstract class SideEffectsModule {

    @Binds
    abstract fun bindToast(toasts: ToastsImpl): Toasts

    @Binds
    abstract fun bindResources(resourcesImpl: ResourcesImpl): Resources

    @Binds
    @Named(INTERMEDIATE_NAVIGATOR_DEPENDENCY)
    abstract fun bindIntermediateNavigator(navigator: IntermediateNavigator): Navigator

    @Binds
    abstract fun bindNavigator(navigator: NavComponentNavigator): Navigator

    companion object {
        const val INTERMEDIATE_NAVIGATOR_DEPENDENCY = "intermediateNavigatorDependency"
    }

}