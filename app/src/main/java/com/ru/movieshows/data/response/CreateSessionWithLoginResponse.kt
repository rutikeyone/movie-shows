package com.ru.movieshows.data.response

import com.google.gson.annotations.SerializedName

data class CreateSessionWithLoginResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("expires_at")
    val expiresAt: String?,
    @SerializedName("request_token")
    val requestToken: String?
)