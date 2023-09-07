package com.ru.movieshows.presentation.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

typealias TryAgainAction = () -> Unit
class LoadStateAdapter(
    private val tryAgainAction: TryAgainAction,
    private val context: Context,
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