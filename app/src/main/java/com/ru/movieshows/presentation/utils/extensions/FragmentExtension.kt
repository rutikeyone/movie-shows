package com.ru.movieshows.presentation.utils.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.ru.movieshows.R
import com.ru.movieshows.domain.entity.PasswordField
import com.ru.movieshows.domain.entity.PasswordValidationStatus
import com.ru.movieshows.domain.entity.UsernameField
import com.ru.movieshows.domain.entity.UsernameValidationStatus
import com.ru.movieshows.presentation.FragmentHolder
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.toast.Toasts

fun BaseFragment.validateUsername(value: UsernameField): String? {
    val s = when (value.status) {
        UsernameValidationStatus.EMPTY -> getString(R.string.empty_text_field)
        UsernameValidationStatus.INVALID -> getString(R.string.invalid_user_name)
        else -> null
    }
    return s
}

fun BaseFragment.validatePassword(value: PasswordField): String? = when(value.status) {
    PasswordValidationStatus.EMPTY -> getString(R.string.empty_text_field)
    PasswordValidationStatus.INVALID -> getString(R.string.the_password_must_contain_more_than_second_characters)
    else -> null
}

fun BaseFragment.hideKeyboard() {
    val inputMethodService = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodService.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun BaseFragment.navigator(): NavigatorWrapper {
    val activity = requireActivity()
    return if(activity is FragmentHolder) {
        activity.navigator()
    } else {
        throw IllegalStateException("Activity must implementation FragmentHolder")
    }
}

fun BaseFragment.toasts(): Toasts {
    val activity = requireActivity()
    return if(activity is FragmentHolder) {
        activity.toasts()
    } else {
        throw IllegalStateException("Activity must implementation FragmentHolder")
    }
}

fun BaseFragment.resources(): Resources {
    val activity = requireActivity()
    return if(activity is FragmentHolder) {
        activity.resources()
    } else {
        throw IllegalStateException("Activity must implementation FragmentHolder")
    }
}
