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

open class BaseRetrofitSource(
    networkConfig: NetworkConfig,
) {

    private val gson = networkConfig.gson

    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string()
            if(body != null) {
                val error = gson.fromJson(body, ErrorResponseBody::class.java)
                throw AppFailure.fromErrorResponseBody(error)
            }
            throw AppFailure.Empty
        } catch (e: SocketTimeoutException) {
            throw AppFailure.Connection
        } catch (e: UnknownHostException) {
            throw AppFailure.Connection
        } catch (e: ConnectException) {
            if(isRussianLanguageTag()) {
                throw AppFailure.Message(R.string.tmdb_service_is_not_available_from_russia)
            } else {
                throw AppFailure.Connection
            }
        } catch (e: Exception) {
            throw AppFailure.Empty
        }
    }

    private fun isRussianLanguageTag(): Boolean {
        val tag = Locale.getDefault().toLanguageTag()
        return tag == russianLanguageTag
    }

    companion object {
        const val russianLanguageTag = "ru-RU"
    }

}