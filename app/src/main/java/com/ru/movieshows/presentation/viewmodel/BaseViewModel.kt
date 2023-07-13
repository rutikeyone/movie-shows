package com.ru.movieshows.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.ru.movieshows.presentation.utils.DialogIntent
import com.ru.movieshows.presentation.utils.MutableLiveEvent
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.PermissionIntent
import com.ru.movieshows.presentation.utils.SnackBarIntent
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.share

open class BaseViewModel: ViewModel(), PermissionListener {
    private val showSnackBarEvent = MutableLiveEvent<SnackBarIntent>()
    val showSnackBarShareEvent = showSnackBarEvent.share()

    private val showDialogEvent = MutableLiveEvent<DialogIntent>()
    val showDialogShareEvent = showDialogEvent.share();

    private val navigationEvent = MutableLiveEvent<NavigationIntent>()
    val navigationShareEvent = navigationEvent.share();

    private val toastEvent = MutableLiveEvent<ToastIntent>()
    val toastShareEvent = toastEvent.share();

    private val permissionEvent = MutableLiveEvent<PermissionIntent>()
    val permissionShareEvent = permissionEvent.share();

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {}
    override fun onPermissionDenied(response: PermissionDeniedResponse?) {}
    override fun onPermissionRationaleShouldBeShown(response: PermissionRequest?, token: PermissionToken?) {}
}