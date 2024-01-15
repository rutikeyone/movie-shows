package com.ru.movieshows.app.model

sealed class Result<T> {

    fun <R> map(mapper: ((T) -> R)? = null): Result<R> {
        return when(this) {
            is Success -> {
                if(mapper == null) {
                    throw IllegalStateException("Can't map Success<T> result without mapper.")
                } else {
                    Success(mapper(this.value))
                }
            }
            is Empty -> Empty()
            is Error -> Error(this.error)
            is Pending -> Pending()
        }
    }

    fun getValueOrNull(): T? {
        if(this is Success<T>) return this.value
        return null
    }

    fun isFinished() = this is Success<T> || this is Error<T>

}

class Success<T>(
    val value: T
): Result<T>()

class Error<T>(
    val error: Throwable,
): Result<T>()

class Empty<T>: Result<T>()

class Pending<T>: Result<T>()