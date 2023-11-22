package com.ru.movieshows.presentation.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ru.movieshows.presentation.contract.navigator
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.observeEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel

open class BaseFragment: Fragment() {

    protected open val viewModel by viewModels<BaseViewModel>()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null && intent.action == Intent.ACTION_LOCALE_CHANGED) {
                viewModel.updateCurrentLanguage()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
        viewModel.navigationShareEvent.observeEvent(this, ::performNavigationEvent)
        viewModel.toastShareEvent.observeEvent(this, ::performToastEvent)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun performToastEvent(toastIntent: ToastIntent) {
        val message = toastIntent.message
        val duration = toastIntent.duration
        Toast.makeText(requireContext(), message, duration).show()
    }

    override fun onDestroyView() {
        requireActivity().unregisterReceiver(broadcastReceiver)
        super.onDestroyView()
    }

    private fun performNavigationEvent(intent: NavigationIntent) {
        when (intent) {
            NavigationIntent.ToPopularMovies -> navigator().navigateToPopularMovies()
            NavigationIntent.ToTopRatedMovies -> navigator().navigateToTopRatedMovies()
            NavigationIntent.ToUpcomingMovies -> navigator().navigateToUpcomingMovies()
            NavigationIntent.ToMovieSearch -> navigator().navigateToMovieSearch()
            NavigationIntent.Pop -> navigator().pop()
            NavigationIntent.ToTvShowsSearch -> navigator().navigateToTvShowsSearch()
            NavigationIntent.ToAirTvShows -> navigator().navigateToAirTvShows()
            NavigationIntent.ToTopRatedTvShows -> navigator().navigateToTopRatedTvShows()
            NavigationIntent.ToTopPopularTvShows -> navigator().navigateToPopularTvShows()
            is NavigationIntent.ToMovieDetails -> navigator().navigateToMovieDetails(intent.id)
            is NavigationIntent.ToReviews -> navigator().navigateToReviews(intent.reviews, intent.movieId)
            is NavigationIntent.ToTvShowsDetails -> navigator().navigateToTvShowDetails(intent.id)
            is NavigationIntent.ToVideo -> navigator().navigateToVideo(intent.video)
        }
    }

    fun setSoftInputMode(mode: Int) = requireActivity().window.setSoftInputMode(mode)
}