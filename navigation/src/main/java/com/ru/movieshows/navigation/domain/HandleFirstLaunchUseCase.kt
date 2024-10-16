package com.ru.movieshows.navigation.domain

import com.ru.movieshows.navigation.domain.repositories.FirstLaunchRepository
import javax.inject.Inject

class HandleFirstLaunchUseCase @Inject constructor(
    private val firstLaunchRepository: FirstLaunchRepository,
){

    fun execute() {
        val firstLaunch = firstLaunchRepository.getFirstLaunch()
        if(firstLaunch) {
            firstLaunchRepository.setFirstLaunch(false)
        }
    }

}