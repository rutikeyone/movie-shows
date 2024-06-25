package com.ru.movieshows.navigation.presentation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor() : ViewModel() {

    private var _selectedItemId: Int? = null

    var selectedItemId: Int?
        get() = _selectedItemId
        set(value) {
            _selectedItemId = value
        }

    private var _navState: Bundle? = null

    var navState: Bundle?
        get() = _navState
        set(value) {
            if (value == null && _navState != null) return
            _navState = value
        }

}