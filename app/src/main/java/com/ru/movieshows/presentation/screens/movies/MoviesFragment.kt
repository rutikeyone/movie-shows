package com.ru.movieshows.presentation.screens.movies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movies.MoviesState
import com.ru.movieshows.presentation.viewmodel.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BaseFragment(R.layout.fragment_movies) {
    override val viewModel by viewModels<MoviesViewModel>()
    private val binding by viewBinding<FragmentMoviesBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderUI)
    }

    private fun renderUI(moviesState: MoviesState) {
        binding.root.children.forEach { it.visibility = View.GONE }
        when (moviesState) {
            MoviesState.InPending -> renderInPendingUI()
            is MoviesState.Success -> {}
            is MoviesState.Failure -> {}
        }
    }

    private fun renderInPendingUI() {
        binding.inPendingContainer.visibility = View.VISIBLE
    }
}
