package com.ru.movieshows.dependencies

import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.resources.ResourcesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourcesModule {

    @Binds
    abstract fun bindResources(resourcesImpl: ResourcesImpl): Resources

}