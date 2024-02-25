package com.ru.movieshows.core.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ru.movieshows.core.AlertDialogConfig
import com.ru.movieshows.core.CommonUi
import com.ru.movieshows.core.Core
import com.ru.movieshows.core.Logger
import com.ru.movieshows.core.Resources
import com.ru.movieshows.core.presentation.live.Event
import com.ru.movieshows.core.presentation.live.LiveEventValue
import com.ru.movieshows.core.presentation.live.LiveValue
import com.ru.movieshows.core.presentation.live.MutableLiveValue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected val viewModelScope: CoroutineScope by lazy {
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            Core.errorHandler.handleError(exception)
        }
        CoroutineScope(SupervisorJob() + Dispatchers.Main + errorHandler)
    }

    protected val resources: Resources get() = Core.resources

    protected val commonUi: CommonUi get() = Core.commonUi

    protected val logger: Logger get() = Core.logger

    protected val debounceFlow = MutableSharedFlow<() -> Unit>(
        replay = 1,
        extraBufferCapacity = 42,
    )

    init {
        viewModelScope.launch {
            debounceFlow.sample(Core.debouncePeriodMillis).collect {
                it()
            }
        }
    }

    protected fun <T> liveValue(): LiveValue<T> {
        return MutableLiveValue()
    }

    protected fun <T> liveValue(value: T): LiveValue<T> {
        return MutableLiveValue(MutableLiveData(value))
    }

    protected var <T> LiveValue<T>.value: T
        get() = this.requireValue()
        set(value) {
            (this as? MutableLiveValue)?.setValue(value)
        }

    protected fun <T> liveEvent(): LiveEventValue<T> {
        return MutableLiveValue()
    }

    protected fun <T> LiveEventValue<T>.publish(event: T) {
        this.value = Event(event)
    }

    protected fun <T> Flow<T>.toLiveValue(initialValue: T?): LiveValue<T> {
        val liveValue = if(initialValue != null) {
            liveValue<T>(initialValue)
        } else {
            liveValue()
        }
        viewModelScope.launch {
            collect {
                liveValue.value = it
            }
        }
        return liveValue
    }

    protected fun debounce(block: () -> Unit) {
        debounceFlow.tryEmit(block)
    }

    protected fun showErrorDialog(message: String) {
        Core.globalScope.launch {
            commonUi.alertDialog(
                AlertDialogConfig(
                    title = resources.getString(R.string.core_presentation_general_error_title),
                    message = message,
                    positiveButton = resources.getString(R.string.core_presentation_general_error_ok),
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}