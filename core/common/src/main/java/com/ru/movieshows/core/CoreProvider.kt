package com.ru.movieshows.core

import kotlinx.coroutines.CoroutineScope

interface CoreProvider {

    val commonUi: CommonUi

    val logger: Logger

    val resources: Resources

    val globalScope: CoroutineScope

    val errorHandler: ErrorHandler

    val appRestarter: AppRestarter

    val screenCommunication: ScreenCommunication

    val remoteTimeoutMillis: Long get() = 10_000L

    val localTimeoutMillis: Long get() = 3_000L

    val debouncePeriodMillis: Long get() = 200L

}