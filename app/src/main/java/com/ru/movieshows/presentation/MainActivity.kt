package com.ru.movieshows.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ru.movieshows.databinding.ActivityMainBinding
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.utils.ToastIntent
import com.ru.movieshows.presentation.utils.observeEvent
import com.ru.movieshows.presentation.viewmodel.main.ActivityScopeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SideEffectHolder {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigator: Navigator

    private val activityScopeViewModel by viewModels<ActivityScopeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        activityScopeViewModel.toastShareEvent.observeEvent(this, ::processToastEvent)
        navigator.injectActivity(this)
    }

    private fun processToastEvent(toastIntent: ToastIntent) {
        val message = toastIntent.message
        val duration = toastIntent.duration
        Toast.makeText(this, message, duration).show()
    }

    override fun onResume() {
        super.onResume()
        activityScopeViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        activityScopeViewModel.navigator.setTarget(null)
    }

    override fun navigator(): NavigatorWrapper = activityScopeViewModel.navigator

}