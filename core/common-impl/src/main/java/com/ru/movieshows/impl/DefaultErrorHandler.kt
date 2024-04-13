package com.ru.movieshows.impl

import com.ru.movieshows.core.AlertDialogConfig
import com.ru.movieshows.core.AppRestarter
import com.ru.movieshows.core.NotAuthException
import com.ru.movieshows.core.CommonUi
import com.ru.movieshows.core.ConnectionException
import com.ru.movieshows.core.ErrorHandler
import com.ru.movieshows.core.Logger
import com.ru.movieshows.core.MessageException
import com.ru.movieshows.core.RemoteServiceException
import com.ru.movieshows.core.Resources
import com.ru.movieshows.core.StorageException
import com.ru.movieshows.core.UserFriendlyException
import com.ru.movieshows.core.impl.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch

class DefaultErrorHandler(
    private val logger: Logger,
    private val commonUi: CommonUi,
    private val resources: Resources,
    private val appRestarter: AppRestarter,
    private val globalScope: CoroutineScope,
) : ErrorHandler {

    private var lastRestartTimestamp = 0L

    override fun handleError(exception: Throwable) {
        logger.err(exception)
        when (exception) {
            is NotAuthException -> handleNotAuthException(exception)
            is ConnectionException -> handleConnectionException(exception)
            is StorageException -> handleStorageException(exception)
            is RemoteServiceException -> handleRemoteServiceException(exception)
            is UserFriendlyException -> handleUserFriendlyException(exception)
            is TimeoutCancellationException -> handleTimeoutException(exception)
            is MessageException -> handleMessageException(exception)
            is CancellationException -> return
            else -> handleUnknownException()
        }
    }

    override fun getUserHeader(exception: Throwable): String {
        return when(exception) {
            is NotAuthException -> resources.getString(R.string.core_to_see_the_profile_data_need_log_in_message)
            is ConnectionException -> resources.getString(R.string.core_common_connect_to_the_internet)
            else -> resources.getString(R.string.core_common_error_header)
        }
    }

    override fun getUserMessage(exception: Throwable): String {
        return when (exception) {
            is ConnectionException -> resources.getString(R.string.core_common_error_connection)
            is StorageException -> resources.getString(R.string.core_common_error_storage)
            is TimeoutCancellationException -> resources.getString(R.string.core_common_error_timeout)
            is RemoteServiceException -> getRemoteServiceExceptionMessage(exception)
            is UserFriendlyException -> exception.userFriendlyMessage
            else -> resources.getString(R.string.core_common_error_message)
        }
    }

    private fun getRemoteServiceExceptionMessage(exception: RemoteServiceException): String {
        val message = exception.message
        return if (message?.isNotBlank() == true) {
            message
        } else {
            resources.getString(R.string.core_common_error_service)
        }
    }

    private fun handleNotAuthException(exception: NotAuthException) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastRestartTimestamp > RESTART_TIMEOUT) {
            commonUi.toast(getUserMessage(exception))
            lastRestartTimestamp = currentTimestamp
            appRestarter.restartApp()
        }
    }

    private fun handleMessageException(exception: MessageException) {
        val message = resources.getString(exception.messageRes)
        commonUi.toast(message)
    }

    private fun handleConnectionException(exception: ConnectionException) {
        commonUi.toast(getUserMessage(exception))
    }

    private fun handleStorageException(exception: StorageException) {
        commonUi.toast(getUserMessage(exception))
    }

    private fun handleTimeoutException(exception: TimeoutCancellationException) {
        commonUi.toast(getUserMessage(exception))
    }

    private fun handleRemoteServiceException(exception: RemoteServiceException) {
        showErrorDialog(getRemoteServiceExceptionMessage(exception))
    }

    private fun handleUserFriendlyException(exception: UserFriendlyException) {
        showErrorDialog(exception.userFriendlyMessage)
    }

    private fun handleUnknownException() {
        val message = resources.getString(R.string.core_common_error_message)
        commonUi.toast(message)
    }

    private fun showErrorDialog(message: String) {
        globalScope.launch {
            commonUi.alertDialog(
                AlertDialogConfig(
                    title = resources.getString(R.string.core_common_error_title),
                    message = message,
                    positiveButton = resources.getString(R.string.core_common_action_ok),
                )
            )
        }
    }

    private companion object {
        const val RESTART_TIMEOUT = 5000
    }
}