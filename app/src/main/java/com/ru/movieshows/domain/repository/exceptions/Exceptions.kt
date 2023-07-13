package com.ru.movieshows.domain.repository.exceptions

class GenresException: IllegalStateException("An error occurred in the process of obtaining genre data")

class MoviesException: IllegalStateException("An error occurred in the process of obtaining videos data")

class TvShowException: IllegalStateException("An error occurred in the process of obtaining tv shows data")