package com.ru.movieshows.presentation.screens.movie_search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import com.ru.movieshows.R
import com.ru.movieshows.presentation.screens.BaseFragment


class MovieSearchFragment : BaseFragment(R.layout.fragment_movie_search) {
    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

    private val toolbar get() = requireActivity().findViewById<MaterialToolbar>(R.id.tabsToolbar)

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
           menuInflater.inflate(R.menu.movie_search_menu, menu)
            searchItem = menu.findItem(R.id.search_action)
            searchView = searchItem?.actionView as SearchView?
            searchView?.maxWidth = Int.MAX_VALUE
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
            R.id.search_action -> true
            androidx.appcompat.R.id.search_button -> true
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.addMenuProvider(menuProvider, viewLifecycleOwner)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        toolbar.removeMenuProvider(menuProvider)
        super.onDestroyView()
    }
}