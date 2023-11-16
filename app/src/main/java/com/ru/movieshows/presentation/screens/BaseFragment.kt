package com.ru.movieshows.presentation.screens

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.ru.movieshows.presentation.contract.navigator
import com.ru.movieshows.presentation.utils.DialogIntent
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.PermissionIntent
import com.ru.movieshows.presentation.utils.SnackBarIntent
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.observeEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.receiver.LanguageChangedReceiver
import com.ru.movieshows.receiver.ReceiverListener

open class BaseFragment: Fragment() {

    protected open val viewModel by viewModels<BaseViewModel>()

    private val receiverListener = object : ReceiverListener {
        override fun update() = viewModel.updateCurrentLanguage()
    }

    private val languageChangedReceiver = LanguageChangedReceiver(receiverListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        observeSideEffectIntents()
        registerLanguageChangedReceiver()
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

    private fun executeNavigationEvent(intent: NavigationIntent) {
        when (intent) {
            is NavigationIntent.ToMovieDetails -> navigator().navigateToMovieDetails(intent.id)
            NavigationIntent.ToPopularMovies -> navigator().navigateToPopularMovies()
            NavigationIntent.ToTopRatedMovies -> navigator().navigateToTopRatedMovies()
            NavigationIntent.ToUpcomingMovies -> navigator().navigateToUpcomingMovies()
            NavigationIntent.ToMovieSearch -> navigator().navigateToMovieSearch()
            NavigationIntent.Pop -> navigator().pop()
            NavigationIntent.ToTvShowsSearch -> navigator().navigateToTvShowsSearch()
            is NavigationIntent.ToTvShowsDetails -> navigator().navigateToTvShowDetails(intent.id)
            NavigationIntent.ToAirTvShows -> navigator().navigateToAirTvShows()
            NavigationIntent.ToTopRatedTvShows -> navigator().navigateToTopRatedTvShows()
            NavigationIntent.ToTopPopularTvShows -> navigator().navigateToPopularTvShows()
            is NavigationIntent.ToReviews -> navigator().navigateToReviews(intent.reviews, intent.movieId)
            is NavigationIntent.ToVideo -> navigator().navigateToVideo(intent.video)
        }
    }

    private fun createToast(toastIntent: ToastIntent) =
        Toast.makeText(requireContext(), toastIntent.message, toastIntent.duration).show()

    private fun registerLanguageChangedReceiver() =
        requireActivity().registerReceiver(languageChangedReceiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))

    private fun unregisterLanguageChangedReceiver() = requireActivity().unregisterReceiver(languageChangedReceiver)

    override fun onDestroy() {
        unregisterLanguageChangedReceiver()
        super.onDestroy()
    }
}