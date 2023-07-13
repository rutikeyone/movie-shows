package com.ru.movieshows.presentation.screens

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.ru.movieshows.presentation.utils.DialogIntent
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.PermissionIntent
import com.ru.movieshows.presentation.utils.SnackBarIntent
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.observeEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel

open class BaseFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId) {
    protected open val viewModel by viewModels<BaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        observeSideEffectIntents()
        super.onCreate(savedInstanceState)
    }

    private fun observeSideEffectIntents() {
        viewModel.showSnackBarShareEvent.observeEvent(this, ::createShowSnackBar)
        viewModel.showDialogShareEvent.observeEvent(this, ::createDialog)
        viewModel.navigationShareEvent.observeEvent(this, ::executeNavigationEvent)
        viewModel.toastShareEvent.observeEvent(this, ::createToast)
        viewModel.permissionShareEvent.observeEvent(this, ::createPermissionRequest)
    }

    private fun createPermissionRequest(permissionIntent: PermissionIntent) {
        Dexter
            .withContext(requireActivity())
            .withPermission(permissionIntent.permission)
            .withListener(viewModel)
            .check()
    }

    private fun createDialog(dialogIntent: DialogIntent) {
        AlertDialog.Builder(requireContext())
            .setTitle(dialogIntent.title)
            .setMessage(dialogIntent.message)
            .setPositiveButton(dialogIntent.setPositiveButton.first) {  _, _ ->
                dialogIntent.setPositiveButton.second()
            }
            .create()
            .show()
    }

    private fun createShowSnackBar(snackBarIntent: SnackBarIntent) {
        val message = getString(snackBarIntent.message)
        Snackbar.make(requireView(), message,  Snackbar.LENGTH_LONG).show()
    }

    private fun executeNavigationEvent(navigationIntent: NavigationIntent) {
        when (navigationIntent) {
            is NavigationIntent.To -> findNavController().navigate(navigationIntent.direction)
            NavigationIntent.Pop -> findNavController().popBackStack()
        }
    }

    private fun createToast(toastIntent: ToastIntent) = Toast.makeText(requireContext(), toastIntent.message, toastIntent.duration).show()
}