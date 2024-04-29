package com.ru.movieshows.profile.domain.entities

data class Account(
    val id: Int?,
    val name: String?,
    val includeAdult: Boolean?,
    val username: String?,
    val avatarPath: String?,
)