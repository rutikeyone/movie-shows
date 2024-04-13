package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.SerializedName

data class AccountDataModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("include_adult") val includeAdult: Boolean?,
    @SerializedName("username") val username: String?,
    @SerializedName("avatar") val avatar: AvatarDataModel?,
)