package com.ru.movieshows.navigation.domain.repositories

interface FirstLaunchRepository {

    fun getFirstLaunch(): Boolean

    fun setFirstLaunch(value: Boolean)

}