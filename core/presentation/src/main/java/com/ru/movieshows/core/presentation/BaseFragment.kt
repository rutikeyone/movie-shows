package com.ru.movieshows.core.presentation

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

open class BaseFragment: Fragment() {

    protected val handler = Handler(Looper.getMainLooper())

    protected open val viewModel by viewModels<BaseViewModel>()

    private val languageChangedBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null && intent.action == Intent.ACTION_LOCALE_CHANGED) {}
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
            UsernameValidationStatus.EMPTY -> getString(R.string.core_presentation_empty_text_field)
            UsernameValidationStatus.INVALID -> getString(R.string.core_presentation_invalid_user_name)
            else -> null
        }
        return result
    }

    fun validatePassword(value: PasswordField): String? = when(value.status) {
        PasswordValidationStatus.EMPTY -> getString(R.string.core_presentation_empty_text_field)
        PasswordValidationStatus.INVALID -> getString(R.string.core_presentation_the_password_must_contain_more_than_second_characters)
        else -> null
    }

    fun hideKeyboard() {
        val inputMethodService = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.hideSoftInputFromWindow(requireView().windowToken, 0)
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