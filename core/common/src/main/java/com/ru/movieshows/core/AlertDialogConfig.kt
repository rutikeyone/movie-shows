package com.ru.movieshows.core

data class AlertDialogConfig(
    val title: String,
    val message: String,
    val positiveButton: String,
    val negativeButton: String? = null,
)