package com.ru.movieshows.impl

import DefaultScreenCommunication
import android.content.Context
import com.ru.movieshows.core.AppRestarter
import com.ru.movieshows.core.CommonUi
import com.ru.movieshows.core.CoreProvider
import com.ru.movieshows.core.ErrorHandler
import com.ru.movieshows.core.Logger
import com.ru.movieshows.core.Resources
import com.ru.movieshows.core.ScreenCommunication
import kotlinx.coroutines.CoroutineScope

class DefaultCoreProvider(
    private val applicationContext: Context,
    override val appRestarter: AppRestarter,
    override val commonUi: CommonUi = AndroidCommonUi(applicationContext),
    override val logger: Logger = AndroidLogger(),
    override val resources: Resources = AndroidResources(),
    override val globalScope: CoroutineScope = createDefaultGlobalScope(),
    override val screenCommunication: ScreenCommunication = DefaultScreenCommunication(),
    override val errorHandler: ErrorHandler = DefaultErrorHandler(logger, commonUi, resources, appRestarter, globalScope),
) : CoreProvider