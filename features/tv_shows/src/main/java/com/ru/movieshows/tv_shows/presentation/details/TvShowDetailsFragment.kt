package com.ru.movieshows.tv_shows.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.BaseScreen
import com.ru.movieshows.core.presentation.args
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentTvShowDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvShowDetailsFragment : BaseFragment() {

    class Screen(
        val id: String,
    ) : BaseScreen

    @Inject
    lateinit var factory: TvShowDetailsViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(args())
    }

    private val binding by viewBinding<FragmentTvShowDetailsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_tv_show_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.root) {
            setTryAgainListener { viewModel.tryGetMovieDetails() }
            observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) { state ->
            }
        }

    }

}