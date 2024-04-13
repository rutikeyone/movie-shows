package com.ru.movieshows.navigation.domain.repositories

interface MainFirstLaunchRepository {

    fun getFirstLaunch(): Boolean

    fun setFirstLaunch(value: Boolean)

}