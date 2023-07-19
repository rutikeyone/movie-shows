package com.ru.movieshows.domain.repository.exceptions

import com.ru.movieshows.R

class GenresException: IllegalStateException("An error occurred in the process of obtaining genre data")

class TvShowException: IllegalStateException("An error occurred in the process of obtaining tv shows data")

sealed class AppFailure : Exception() {
    object Pure: AppFailure()
    object Connection: AppFailure()

    fun headerResource(): Int {
        return when(this) {
            Connection -> R.string.connect_to_the_internet
            Pure -> R.string.error_header
        }
    }

    fun errorResource(): Int {
        return when(this) {
            Connection -> R.string.check_your_connection
            Pure -> R.string.an_error_occurred_during_the_operation
        }
    }
}