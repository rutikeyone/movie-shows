package com.ru.movieshows.domain.entity

import java.lang.Exception


sealed class Result<T> {
    data class Success<T> (val result: T): Result<T>()
    data class Failure(val exception: Exception): Result<Exception>()
}