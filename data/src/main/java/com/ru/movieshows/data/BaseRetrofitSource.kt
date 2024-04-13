package com.ru.movieshows.data

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.core.ConnectionException
import com.ru.movieshows.core.MessageException
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


data class ErrorDataModel(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("status_message") val statusMessage: String?,
)

open class BaseRetrofitSource(
    private val gson: Gson,
) {

    suspend fun <T, R> Call<T>.awaitResult(map: (T) -> R): R =
        suspendCancellableCoroutine { continuation ->
            try {
                enqueue(object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        if (response.isSuccessful) {
                            try {
                                val result = map(response.body()!!)
                                continuation.resume(result)
                            } catch (throwable: Throwable) {
                                continuation.resumeWithException(throwable)
                            }
                        } else {
                            val exception = asNetworkException(HttpException(response))
                            continuation.resumeWithException(exception)
                        }
                    }

                    override fun onFailure(call: Call<T>, t: Throwable) {
                        val exception = asNetworkException(t)
                        continuation.resumeWithException(exception)
                    }

                })
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }

    private fun asNetworkException(exception: Throwable): Throwable {
        return when (exception) {
            is IOException -> ConnectionException(exception)
            is HttpException -> extractHttpException(exception)
            else -> exception
        }
    }

    @SuppressLint("ResourceType")
    private fun extractHttpException(e: HttpException): Exception {
        val body = e.response()?.errorBody()?.string()
        return if (body != null) {
            val errorDataModel = gson.fromJson(body, ErrorDataModel::class.java)
            val statusCode = errorDataModel.statusCode
            val message = when (statusCode) {
                ACCOUNT_DISABLED_CODE -> R.string.your_account_is_no_longer_active_contact_tmdb_message
                ACCOUNT_NOT_VERIFIED_CODE -> R.string.the_email_address_has_not_been_confirmed
                INVALID_USERNAME_OR_PASSWORD_CODE -> R.string.invalid_username_or_password
                SESSION_DENIED -> R.string.session_creation_denied
                else -> R.string.an_error_occurred_during_the_operation
            }
            MessageException(
                messageRes = message,
                cause = e
            )

        } else {
            MessageException(
                messageRes = R.string.an_error_occurred_during_the_operation,
                cause = e
            )
        }
    }

    private companion object {
        private const val ACCOUNT_DISABLED_CODE = 31
        private const val ACCOUNT_NOT_VERIFIED_CODE = 32;
        private const val INVALID_USERNAME_OR_PASSWORD_CODE = 30
        private const val SESSION_DENIED = 17
    }

}

