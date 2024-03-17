package com.ru.movieshows.app.presentation.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

typealias TryAgainAction = () -> Unit

class LoadStateAdapter(
    private val context: Context,
    private val tryAgainAction: TryAgainAction,
): LoadStateAdapter<DefaultLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: DefaultLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): DefaultLoadStateViewHolder {
        return DefaultLoadStateViewHolder.create(parent, context, tryAgainAction)
    }

}