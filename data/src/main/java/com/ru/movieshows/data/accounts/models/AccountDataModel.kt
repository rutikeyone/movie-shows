package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.data.accounts.converters.AvatarJsonDeserializer

data class AccountDataModel(
    val id: Int?,
    val name: String?,
    @SerializedName("include_adult")
    val includeAdult: Boolean?,
    val username: String?,
    @SerializedName("avatar")
    @JsonAdapter(value = AvatarJsonDeserializer::class)
    val avatar: String?,
)