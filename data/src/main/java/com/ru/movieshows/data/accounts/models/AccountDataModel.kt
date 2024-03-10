package com.ru.movieshows.data.accounts.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.ershov.data.core.deserializers.AvatarDeserializer

data class AccountDataModel(
    val id: Int?,
    val name: String?,
    @SerializedName("include_adult")
    val includeAdult: Boolean?,
    val username: String?,
    @SerializedName("avatar")
    @JsonAdapter(value = AvatarDeserializer::class)
    val avatar: String?,
)