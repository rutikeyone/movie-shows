package com.ru.movieshows.core.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.ru.movieshows.core.AuthException
import com.ru.movieshows.core.ConnectionException
import com.ru.movieshows.core.Container
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.presentation.R
import com.ru.movieshows.core.presentation.databinding.CorePresentatiionPartResultBinding

class ResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    var container: Container<*> = Container.Pending
        set(value) {
            field = value
            notifyUpdates()
        }

    private var tryAgainListener: (() -> Unit)? = null

    private val binding: CorePresentatiionPartResultBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CorePresentatiionPartResultBinding.inflate(inflater, this, false)
        addView(binding.root)

        if(isInEditMode) {
            container = Container.Pending
        } else {
            with(binding) {
                internalResultContainer.isVisible = false
                progressGroupView.isVisible = false
                failureGroupView.isVisible = false
                children.forEach {
                    it.isVisible = false
                }
            }
            container = Container.Pending
        }

        binding.tryAgainButton.setOnClickListener {
            if(isAuthError()) {
                Core.appRestarter.restartApp()
            } else {
                tryAgainListener?.invoke()
            }
        }

    }

    fun setTryAgainListener(onTryAgain: () -> Unit) {
        this.tryAgainListener = onTryAgain
    }

    private fun notifyUpdates() {
        val container = this.container
        binding.progressGroupView.isVisible = container is Container.Pending
        binding.failureGroupView.isVisible = container is Container.Error
        binding.internalResultContainer.isVisible = container !is Container.Success

        if(container is Container.Error) {
            val exception = container.exception
            Core.logger.err(exception)
            binding.failureTextHeader.text = Core.errorHandler.getUserHeader(exception)
            binding.failureTextMessage.text = Core.errorHandler.getUserMessage(exception)
            binding.tryAgainButton.setText(
                if(isAuthError()) {
                    R.string.core_presentation_logout
                } else {
                    R.string.core_presentation_try_again
                }
            )

        }

        children.forEach {
            if (it != binding.root) {
                it.isVisible = container is Container.Success
            }
        }
    }

    private fun isAuthError() = container.let {
        it is Container.Error && it.exception is AuthException
    }

}