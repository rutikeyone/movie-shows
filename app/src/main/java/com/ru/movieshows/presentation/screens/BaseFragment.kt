package com.ru.movieshows.presentation.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ru.movieshows.R
import com.ru.movieshows.domain.entity.PasswordField
import com.ru.movieshows.domain.entity.PasswordValidationStatus
import com.ru.movieshows.domain.entity.UsernameField
import com.ru.movieshows.domain.entity.UsernameValidationStatus
import com.ru.movieshows.presentation.FragmentHolder
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.presentation.viewmodel.BaseViewModel

open class BaseFragment: Fragment() {

    protected open val viewModel by viewModels<BaseViewModel>()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null && intent.action == Intent.ACTION_LOCALE_CHANGED) {
                viewModel.updateLanguageTag()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        requireActivity().unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }

    fun setSoftInputMode(mode: Int) = requireActivity().window.setSoftInputMode(mode)

    fun validateUsername(value: UsernameField): String? {
        val result = when (value.status) {
            UsernameValidationStatus.EMPTY -> getString(R.string.empty_text_field)
            UsernameValidationStatus.INVALID -> getString(R.string.invalid_user_name)
            else -> null
        }
        return result
    }

    fun validatePassword(value: PasswordField): String? = when(value.status) {
        PasswordValidationStatus.EMPTY -> getString(R.string.empty_text_field)
        PasswordValidationStatus.INVALID -> getString(R.string.the_password_must_contain_more_than_second_characters)
        else -> null
    }

    fun hideKeyboard() {
        val inputMethodService = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun navigator(): NavigatorWrapper {
        val activity = requireActivity()
        return if(activity is FragmentHolder) {
            activity.navigator()
        } else {
            throw IllegalStateException("Activity must implementation FragmentHolder")
        }
    }

    fun toasts(): Toasts {
        val activity = requireActivity()
        return if(activity is FragmentHolder) {
            activity.toasts()
        } else {
            throw IllegalStateException("Activity must implementation FragmentHolder")
        }
    }

    fun resources(): Resources {
        val activity = requireActivity()
        return if(activity is FragmentHolder) {
            activity.resources()
        } else {
            throw IllegalStateException("Activity must implementation FragmentHolder")
        }
    }

}