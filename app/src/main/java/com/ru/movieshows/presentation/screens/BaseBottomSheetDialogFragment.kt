package com.ru.movieshows.presentation.screens

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.presentation.FragmentHolder
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator

open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    fun navigator(): Navigator {
        val activity = requireActivity()
        return if(activity is FragmentHolder) {
            activity.navigator()
        } else {
            throw IllegalStateException("Activity must implementation FragmentHolder")
        }
    }

}