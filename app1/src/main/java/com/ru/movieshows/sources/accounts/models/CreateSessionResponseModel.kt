package com.ru.movieshows.sources.accounts.models

import com.google.gson.annotations.SerializedName

data class CreateSessionResponseModel(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)