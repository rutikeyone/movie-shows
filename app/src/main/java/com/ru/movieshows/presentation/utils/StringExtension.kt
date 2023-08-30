package com.ru.movieshows.presentation.utils

import java.util.regex.Pattern

fun String.getYouTubeId(): String? {
    val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
    val compiledPattern = Pattern.compile(pattern)
    val matcher = compiledPattern.matcher(this)
    return if (matcher.find()) {
        matcher.group()
    } else {
        "error"
    }
}