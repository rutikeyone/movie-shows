package com.ru.movieshows.presentation.screens.tv_show_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.ru.movieshows.R
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.screens.movie_details.MovieDetailsFragmentArgs
import com.ru.movieshows.presentation.viewmodel.movie_details.MovieDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.tv_show_details.TvShowDetailsViewModel
import com.ru.movieshows.presentation.viewmodel.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvShowDetailsFragment : BaseFragment() {
    private val args by navArgs<TvShowDetailsFragmentArgs>()

    @Inject
    lateinit var factory: TvShowDetailsViewModel.Factory
    override val viewModel by viewModelCreator { factory.create(args.id) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tv_show_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner, ::renderTitle)
    }


    private fun renderTitle(value: String?) {
        val toolBar = activity?.findViewById<MaterialToolbar>(R.id.tabsToolbar) ?: return
        toolBar.title = value
    }
}