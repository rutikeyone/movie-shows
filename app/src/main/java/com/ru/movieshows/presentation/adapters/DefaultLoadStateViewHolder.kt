package com.ru.movieshows.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.R
import com.ru.movieshows.databinding.PartDefaultLoadStateBinding
import com.ru.movieshows.domain.repository.exceptions.AppFailure

class DefaultLoadStateViewHolder(
    private val binding: PartDefaultLoadStateBinding,
    private val tryAgainRetry: TryAgainAction,
    private val context: Context,
): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.failurePart.retryButton.setOnClickListener { tryAgainRetry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.failurePart.root.isVisible = loadState is LoadState.Error
        binding.progressBar.isVisible = loadState is LoadState.Loading

        if(loadState is LoadState.Error) {
            val headerError = (loadState.error as? AppFailure)?.headerResource() ?: R.string.error_header
            val messageError = (loadState.error as? AppFailure)?.errorResource() ?: R.string.an_error_occurred_during_the_operation
            val header = context.resources.getString(headerError)
            val error = context.resources.getString(messageError)
            binding.failurePart.failureTextHeader.text = header
            binding.failurePart.failureTextMessage.text = error
        }
    }

    companion object {
        fun create(parent: ViewGroup, context: Context, retry: TryAgainAction): DefaultLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.part_default_load_state, parent, false)
            val binding = PartDefaultLoadStateBinding.bind(view)
            return DefaultLoadStateViewHolder(binding, retry, context)
        }
    }
}