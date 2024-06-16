package com.ru.movieshows.core

import kotlinx.coroutines.CoroutineScope

object Core {

    private lateinit var coreProvider: CoreProvider

    val commonUi: CommonUi get() = coreProvider.commonUi

    val resources: Resources get() = coreProvider.resources

    val logger: Logger get() = coreProvider.logger

    val globalScope: CoroutineScope get() = coreProvider.globalScope

    val errorHandler: ErrorHandler get() = coreProvider.errorHandler

    val appRestarter: AppRestarter get() = coreProvider.appRestarter

    val screenCommunication: ScreenCommunication get() = coreProvider.screenCommunication

    val localTimeoutMillis: Long get() = coreProvider.localTimeoutMillis

    val remoteTimeoutMillis: Long get() = coreProvider.remoteTimeoutMillis

    val debouncePeriodMillis: Long get() = coreProvider.debouncePeriodMillis

    val loaderOverlay: LoaderOverlay get() = coreProvider.loaderOverlay

    fun init(coreProvider: CoreProvider) {
        this.coreProvider = coreProvider
    }

}