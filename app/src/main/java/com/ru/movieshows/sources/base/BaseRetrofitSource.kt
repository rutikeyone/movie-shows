package com.ru.movieshows.sources.base

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale

data class ErrorResponseBody(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_message")
    val statusMessage: String?
)

open class BaseRetrofitSource(networkConfig: NetworkConfig) {

    private val gson = networkConfig.gson

    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: Exception) {
            when(e) {
                is HttpException -> throw createThrowFromHttpException(e)
                is SocketTimeoutException -> throw AppFailure.Connection
                is ConnectException -> throw createThrowFromConnectionException()
                is UnknownHostException -> throw AppFailure.Connection
                else -> throw AppFailure.Empty
            }
        }
    }

    private fun isRussianLanguageTag(): Boolean {
        val tag = Locale.getDefault().toLanguageTag()
        return tag == russianLanguageTag
    }

    private fun createThrowFromHttpException(e: HttpException): AppFailure {
        val body = e.response()?.errorBody()?.string()
        if(body != null) {
            val error = gson.fromJson(body, ErrorResponseBody::class.java)
            return AppFailure.fromErrorResponseBody(error)
        }
        return AppFailure.Empty
    }

    private fun createThrowFromConnectionException(): AppFailure {
        if(isRussianLanguageTag()) {
            return AppFailure.Message(R.string.tmdb_service_is_not_available_from_russia)
        }
        return AppFailure.Connection
    }

    companion object {
        const val russianLanguageTag = "ru-RU"
    }
}