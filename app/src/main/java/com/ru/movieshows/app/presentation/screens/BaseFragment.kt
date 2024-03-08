package com.ru.movieshows.app.presentation.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.FragmentHolder
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.sideeffects.resources.Resources
import com.ru.movieshows.app.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.sources.accounts.entities.PasswordField
import com.ru.movieshows.sources.accounts.entities.PasswordValidationStatus
import com.ru.movieshows.sources.accounts.entities.UsernameField
import com.ru.movieshows.sources.accounts.entities.UsernameValidationStatus

open class BaseFragment: Fragment() {

    protected val handler = Handler(Looper.getMainLooper())

    protected open val viewModel by viewModels<BaseViewModel>()

    private val languageChangedBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null && intent.action == Intent.ACTION_LOCALE_CHANGED) {
                viewModel.updateLanguageTag()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val intentFilter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        requireActivity().registerReceiver(languageChangedBroadcastReceiver, intentFilter)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(languageChangedBroadcastReceiver)
        super.onDestroy()
    }

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

    fun navigator(): Navigator {
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

    protected fun getSpanCount(
        @DimenRes dimen: Int = R.dimen.dp_120
    ): Int {
        val context = requireContext()
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        val widthPixels = displayMetrics.widthPixels
        val width = (widthPixels / density).toInt()
        val resourceDimension = context.resources.getDimension(dimen).toInt()
        val itemWidth = (resourceDimension / density).toInt()
        return width / itemWidth
    }

}