package com.ru.movieshows.presentation.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.ru.movieshows.R
import com.ru.movieshows.domain.entity.EmailField
import com.ru.movieshows.domain.entity.EmailValidationStatus
import com.ru.movieshows.domain.entity.PasswordField
import com.ru.movieshows.domain.entity.PasswordValidationStatus
import com.ru.movieshows.presentation.screens.BaseFragment

fun BaseFragment.validateEmail(value: EmailField): String? = when(value.status) {
    EmailValidationStatus.EMPTY -> getString(R.string.empty_text_field)
    EmailValidationStatus.INVALID_EMAIL -> getString(R.string.incorrect_email)
    else -> null
}

fun BaseFragment.validatePassword(value: PasswordField): String? = when(value.status) {
    PasswordValidationStatus.EMPTY -> getString(R.string.empty_text_field)
    PasswordValidationStatus.INVALID -> getString(R.string.the_password_must_contain_more_than_second_characters)
    else -> null
}

fun Fragment.hideKeyboard() {
    val inputMethodService = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodService.hideSoftInputFromWindow(requireView().windowToken, 0)
}