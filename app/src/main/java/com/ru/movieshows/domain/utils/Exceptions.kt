package com.ru.movieshows.domain.utils

import androidx.annotation.StringRes
import com.ru.movieshows.R

sealed class AppFailure : Exception() {
    object Pure: AppFailure()
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
            Pure -> R.string.an_error_occurred_during_the_operation
            is Message -> value
        }
    }
}