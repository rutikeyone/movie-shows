package com.ru.movieshows.data.model

import com.google.gson.annotations.SerializedName
import com.ru.movieshows.domain.entity.AccountEntity

data class AccountModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("include_adult")
    val includeAdult: Boolean?,
    @SerializedName("username")
    val username: String?,
) {
    fun toEntity() : AccountEntity = AccountEntity(
        id,
        name,
        includeAdult,
        username,
    )
}