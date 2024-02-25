package com.ru.movieshows.core.presentation

import android.os.Build
import androidx.fragment.app.Fragment

const val ARG_SCREEN = "screen"

inline fun <reified T : BaseScreen> Fragment.args(): T {
    val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requireArguments().getSerializable(ARG_SCREEN, T::class.java)
    } else {
        requireArguments().getSerializable(ARG_SCREEN) as? T
    }
    return value ?: throw IllegalStateException("Screen args don't exist")
}