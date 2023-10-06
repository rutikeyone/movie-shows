package com.ru.movieshows.presentation.screens.tv_show_search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FragmentMoviesBinding
import com.ru.movieshows.databinding.FragmentTvShowSearchBinding
import com.ru.movieshows.presentation.screens.BaseFragment
import com.ru.movieshows.presentation.utils.viewBinding
import com.ru.movieshows.presentation.viewmodel.movies.MoviesViewModel
import com.ru.movieshows.presentation.viewmodel.tv_show_search.TvShowSearchViewModel
import com.ru.movieshows.presentation.viewmodel.tv_shows.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowSearchFragment : BaseFragment(R.layout.fragment_tv_show_search) {
    override val viewModel by viewModels<TvShowSearchViewModel>()
    private val binding by viewBinding<FragmentTvShowSearchBinding>()

    private val toolbar get() = requireActivity().findViewById<MaterialToolbar>(R.id.tabsToolbar)

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_view_menu, menu)
            searchItem = searchItem ?: menu.findItem(R.id.search_action)
            searchView = searchView ?: searchItem?.actionView as SearchView?
            searchView?.maxWidth = Int.MAX_VALUE

            searchView?.setOnQueryTextListener(onQueryTextListener)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
            R.id.search_action -> true
            androidx.appcompat.R.id.search_button -> true
            else -> false
        }

        private val onQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.addMenuProvider(menuProvider, viewLifecycleOwner)
        if(!viewModel.expanded) {
            searchItem?.expandActionView()
            viewModel.setExpanded(true)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        toolbar.removeMenuProvider(menuProvider)
        super.onDestroyView()
    }

    override fun onDestroy() {
        searchItem = null
        searchView = null
        super.onDestroy()
    }
}