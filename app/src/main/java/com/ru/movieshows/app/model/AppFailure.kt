package com.ru.movieshows.app.model

import androidx.annotation.StringRes
import com.ru.movieshows.R
import com.ru.movieshows.sources.base.ErrorResponseBody


sealed class AppFailure : Exception() {
    object Empty: AppFailure()
    object Connection: AppFailure()
    data class Message(@StringRes val value: Int) : AppFailure()


    fun headerResource(): Int {
        return when(this) {
            Connection -> R.string.connect_to_the_internet
            else -> R.string.error_header
        }
    }

    fun errorResource(): Int {
        return when(this) {
            Connection -> R.string.check_your_connection
            Empty -> R.string.an_error_occurred_during_the_operation
            is Message -> value
        }
    }

    companion object {

        fun fromErrorResponseBody(accountError: ErrorResponseBody): AppFailure {
            return when(accountError.statusCode) {
                accountDisabledCode -> Message(R.string.your_account_is_no_longer_active_contact_tmdb_message)
                accountNotVerifiedCode -> Message(R.string.the_email_address_has_not_been_confirmed)
                invalidUsernameOrPasswordCode -> Message(R.string.invalid_username_or_password)
                sessionDenied -> Message(R.string.session_creation_denied)
                else -> Empty
            }
        }

        private const val accountDisabledCode = 31
        private const val accountNotVerifiedCode = 32;
        private const val invalidUsernameOrPasswordCode = 30
        private const val sessionDenied = 17
    }
}