package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.SerializedName

data class CreateSessionModel(
    @SerializedName("success") val success: Boolean,
    @SerializedName("session_id") val sessionId: String,
)