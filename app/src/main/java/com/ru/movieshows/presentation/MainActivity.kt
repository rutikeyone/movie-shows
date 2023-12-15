package com.ru.movieshows.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ru.movieshows.databinding.ActivityMainBinding
import com.ru.movieshows.presentation.sideeffects.loader.LoaderOverlay
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.sideeffects.resources.Resources
import com.ru.movieshows.presentation.sideeffects.toast.Toasts
import com.ru.movieshows.presentation.viewmodel.main.ActivityScopeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentHolder {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigator: Navigator

    private val activityScopeViewModel by viewModels<ActivityScopeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        navigator.injectActivity(this)
        activityScopeViewModel.loaderOverlayState.observe(this) {
            binding.loaderOverlay.progressOverlay.isVisible = it
        }
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

    override fun toasts(): Toasts = activityScopeViewModel.toasts

    override fun resources(): Resources = activityScopeViewModel.resources

    override fun loaderOverlay(): LoaderOverlay = activityScopeViewModel

}