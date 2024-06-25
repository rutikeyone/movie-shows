package com.ru.movieshows.core.presentation.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.core.AppException
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.presentation.databinding.CorePartDefaultLoadStateBinding

typealias TryAgainAction = () -> Unit

class DefaultLoadStateViewHolder(
    private val context: Context,
    private val binding: CorePartDefaultLoadStateBinding,
    private val tryAgainRetry: TryAgainAction,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.failurePart.retryButton.setOnClickListener {
            tryAgainRetry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        binding.failurePart.root.isVisible = loadState is LoadState.Error
        binding.progressBar.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error) {
            val exception = AppException(cause = loadState.error)

            val headerHeaderText = Core.errorHandler.getUserHeader(exception)
            val messageText = Core.errorHandler.getUserMessage(exception)

            binding.failurePart.failureHeaderTextView.text = headerHeaderText
            binding.failurePart.failureMessageTextView.text = messageText
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            context: Context,
            retry: TryAgainAction,
        ): DefaultLoadStateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CorePartDefaultLoadStateBinding.inflate(inflater, parent, false)
            return DefaultLoadStateViewHolder(context, binding, retry)
        }

    }

}