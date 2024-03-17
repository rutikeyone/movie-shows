package com.ru.movieshows.sources.accounts.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.sources.accounts.converters.AvatarConverter
import com.ru.movieshows.sources.accounts.entities.AccountEntity

data class AccountModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("include_adult")
    val includeAdult: Boolean?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("avatar")
    @JsonAdapter(value = AvatarConverter::class)
    val avatar: String?,
) {
    fun toEntity(imageUrl: String) : AccountEntity = AccountEntity(
        id,
        name,
        includeAdult,
        username,
        if(this.avatar != null) imageUrl + this.avatar else null,
    )
}