package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.SerializedName

data class AuthenticationModel(
    val success: Boolean,
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val requestToken: String,
)