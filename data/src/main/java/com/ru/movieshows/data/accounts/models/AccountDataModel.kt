package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.SerializedName

data class AccountDataModel(
    val id: Int?,
    val name: String?,
    @SerializedName("include_adult")
    val includeAdult: Boolean?,
    val username: String?,
    @SerializedName("avatar")
    val avatar: String?,
)