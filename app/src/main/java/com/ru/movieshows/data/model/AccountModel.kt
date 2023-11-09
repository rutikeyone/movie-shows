package com.ru.movieshows.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.ru.movieshows.BuildConfig
import com.ru.movieshows.data.utils.ProfileAvatarConverter
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
    @SerializedName("avatar")
    @JsonAdapter(value = ProfileAvatarConverter::class)
    val avatar: String?,
) {
    fun toEntity() : AccountEntity = AccountEntity(
        id,
        name,
        includeAdult,
        username,
        if(this.avatar != null) BuildConfig.TMDB_IMAGE_URL + this.avatar else null,
    )
}