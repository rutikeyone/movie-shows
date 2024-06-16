package com.ru.movieshows.navigation.domain

import com.ru.movieshows.navigation.domain.repositories.MainFirstLaunchRepository
import javax.inject.Inject

class SetFirstLaunchUseCase @Inject constructor(
    private val mainFirstLaunchRepository: MainFirstLaunchRepository,
){

    fun setupLaunch() {
        val firstLaunch = mainFirstLaunchRepository.getFirstLaunch()
        if(firstLaunch) {
            mainFirstLaunchRepository.setFirstLaunch(false)
        }
    }

}