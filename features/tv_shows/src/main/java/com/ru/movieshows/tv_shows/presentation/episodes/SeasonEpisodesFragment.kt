package com.ru.movieshows.tv_shows.presentation.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ru.movieshows.core.presentation.BaseFragment
import com.ru.movieshows.core.presentation.BaseScreen
import com.ru.movieshows.core.presentation.applyDecoration
import com.ru.movieshows.core.presentation.args
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.ItemDecoration
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.tv_shows.domain.entities.Episode
import com.ru.movieshows.tv_shows.presentation.views.EpisodesAdapter
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentSeasonEpisodesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SeasonEpisodesFragment : BaseFragment() {

    data class Screen(
        val seriesId: String,
        val seasonNumber: String,
    ) : BaseScreen

    @Inject
    lateinit var factory: SeasonEpisodesViewModel.Factory

    override val viewModel by viewModelCreator {
        factory.create(args())
    }

    private val binding by viewBinding<FragmentSeasonEpisodesBinding>()

    private val episodeAdapter by lazy {
        EpisodesAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_season_episodes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding.root) {
            setTryAgainListener { viewModel.toTryAgain() }
            observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) { state ->
                setupViews(state)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViews(episodes: List<Episode>) {

        episodeAdapter.submitData(episodes)

        val spanCount = getSpanCount()
        val itemDecoration = ItemDecoration(
            spanCount = spanCount,
        )
        val gridLayoutManager = GridLayoutManager(context, spanCount)

        with(binding.episodesRecyclerView) {
            adapter = episodeAdapter
            layoutManager = gridLayoutManager
            applyDecoration(itemDecoration)
        }
    }

}