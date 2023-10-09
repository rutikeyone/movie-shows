package com.ru.movieshows.presentation.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Listener<T>(val clickAction: (data: T) -> Unit): Parcelable {
    fun onClick(data: T) = clickAction(data)
}