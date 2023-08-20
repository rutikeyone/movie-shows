package com.ru.movieshows.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ru.movieshows.R
import com.ru.movieshows.databinding.FailurePartBinding
import com.ru.movieshows.databinding.PartDefaultLoadStateBinding
import com.ru.movieshows.domain.repository.exceptions.AppFailure

typealias TryAgainAction = () -> Unit
class DefaultLoadStateAdapter(
    private val tryAgainAction: TryAgainAction,
    private val context: Context,
): LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PartDefaultLoadStateBinding.inflate(inflater, parent, false)
        return Holder(binding, null, tryAgainAction, context)
    }
    class Holder(
        private val binding: PartDefaultLoadStateBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val tryAgainAction: TryAgainAction,
        private val context: Context,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = with(binding) {
            setupFailurePart(loadState)
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.isRefreshing = loadState is LoadState.Loading
                progressBar.isVisible = false
            } else {
                progressBar.isVisible = loadState is LoadState.Loading
            }
        }

        private fun setupFailurePart(loadState: LoadState) {
            binding.failurePart.root.isVisible = loadState is LoadState.Error
            if (loadState is LoadState.Error) {
                binding.failurePart.root.forEach { it.isVisible = true }
                val headerError =
                    if (loadState.error is AppFailure) (loadState.error as AppFailure).headerResource() else R.string.error_header
                val messageError =
                    if (loadState.error is AppFailure) (loadState.error as AppFailure).errorResource() else R.string.an_error_occurred_during_the_operation
                val header = context.resources.getString(headerError)
                val message = context.resources.getString(messageError)
                binding.failurePart.failureTextHeader.text = header
                binding.failurePart.failureTextMessage.text = message
                binding.failurePart.retryButton.setOnClickListener { tryAgainAction() }
            } else {
                binding.failurePart.root.forEach { it.isVisible = false }
            }
        }
    }

}