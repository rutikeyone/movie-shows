package com.ru.movieshows.domain.repository

import com.google.gson.Gson
import com.ru.movieshows.R
import com.ru.movieshows.data.response.AccountErrorModel
import com.ru.movieshows.domain.utils.AppFailure
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale

@Suppress("UNREACHABLE_CODE")
interface Repository {

    fun handleError(e: Exception, gson: Gson): AppFailure {
        return when (e) {
            is retrofit2.HttpException -> {
                val response = e.response()
                val errorBody = response?.errorBody()
                val content = errorBody?.charStream()?.readText()
                if (content != null) {
                    val accountError = gson.fromJson(content, AccountErrorModel::class.java)
                    return AppFailure.fromAccountError(accountError)
                }
                return AppFailure.Pure
            }
            is SocketTimeoutException -> {
                return AppFailure.Connection
            }
            is UnknownHostException -> {
                return AppFailure.Connection
            }
            is ConnectException -> {
                if(isRussianTag()) {
                    return AppFailure.Message(R.string.tmdb_service_is_not_available_from_russia)
                } else {
                    return AppFailure.Connection
                }
            }
            else -> {
                return AppFailure.Pure
            }
        }
    }

    private fun isRussianTag(): Boolean {
        val tag = Locale.getDefault().toLanguageTag()
        return tag == AccountRepositoryImpl.russianLanguageTag
    }

}