package com.ru.movieshows.sources.accounts.models

import com.google.gson.annotations.SerializedName

data class AuthenticationResponseModel(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val requestToken: String
)