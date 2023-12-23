package com.ru.movieshows.presentation.screens.season_episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.ru.movieshows.databinding.FragmentSeasonEpisodesBinding
import com.ru.movieshows.presentation.screens.BaseFragment

class SeasonEpisodesFragment : BaseFragment() {

    private val arguments by navArgs<SeasonEpisodesFragmentArgs>()

    private lateinit var binding: FragmentSeasonEpisodesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeasonEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

}