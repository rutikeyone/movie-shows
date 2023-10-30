package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName

data class CreateSessionWithLoginErrorBody(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_message")
    val statusMessage: String?
)