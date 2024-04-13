package com.ru.movieshows.navigation.domain.exceptions

import com.ru.movieshows.core.AppException

class ActivityNotCreatedException(cause: Exception? = null) : AppException(cause = cause)
