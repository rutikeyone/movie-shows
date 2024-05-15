package com.ru.movieshows.wiring

import android.content.Context
import com.ru.movieshows.core.AppRestarter
import com.ru.movieshows.core.CommonUi
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.CoreProvider
import com.ru.movieshows.core.ErrorHandler
import com.ru.movieshows.core.Logger
import com.ru.movieshows.core.Resources
import com.ru.movieshows.core.ScreenCommunication
import com.ru.movieshows.core.flow.DefaultLazyFlowSubjectFactory
import com.ru.movieshows.core.flow.LazyFlowSubjectFactory
import com.ru.movieshows.impl.ActivityRequired
import com.ru.movieshows.impl.AndroidResources
import com.ru.movieshows.impl.DefaultCoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreProviderModule {

    @Provides
    fun provideCoreProvider(
        @ApplicationContext context: Context,
        appRestarter: AppRestarter,
    ): CoreProvider {
        return DefaultCoreProvider(
            applicationContext = context,
            appRestarter = appRestarter,
        )
    }

    @Provides
    @ElementsIntoSet
    fun provideActivityRequiredSet(
        commonUi: CommonUi,
        screenCommunication: ScreenCommunication,
        resources: Resources,
    ): Set<@JvmSuppressWildcards ActivityRequired> {
        val set = hashSetOf<ActivityRequired>()
        if (commonUi is ActivityRequired) set.add(commonUi)
        if (screenCommunication is ActivityRequired) set.add(screenCommunication)
        if(resources is AndroidResources) set.add(resources)
        return set
    }

    @Provides
    fun provideScreenCommunication(): ScreenCommunication {
        return Core.screenCommunication
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return Core.globalScope
    }

    @Provides
    fun provideCommonUi(): CommonUi {
        return Core.commonUi
    }

    @Provides
    fun providerLogger(): Logger {
        return Core.logger
    }

    @Provides
    fun provideResources(): Resources {
        return Core.resources
    }


    @Provides
    fun provideErrorHandler(): ErrorHandler {
        return Core.errorHandler
    }

    @Provides
    @Singleton
    fun provideLazyFlowSubjectFactory(): LazyFlowSubjectFactory {
        return DefaultLazyFlowSubjectFactory(
            dispatcher = Dispatchers.IO,
        )
    }

}
