package com.ru.movieshows.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ru.movieshows.R
import com.ru.movieshows.databinding.ActivityMainBinding
import com.ru.movieshows.databinding.FragmentTabsBinding
import com.ru.movieshows.domain.entity.ReviewEntity
import com.ru.movieshows.domain.entity.VideoEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.contract.Navigator
import com.ru.movieshows.presentation.screens.air_tv_shows.AirTvShowsFragmentDirections
import com.ru.movieshows.presentation.screens.movie_details.MovieDetailsFragmentDirections
import com.ru.movieshows.presentation.screens.movie_search.MovieSearchFragmentDirections
import com.ru.movieshows.presentation.screens.movies.MoviesFragmentDirections
import com.ru.movieshows.presentation.screens.popular_movies.PopularMoviesFragmentDirections
import com.ru.movieshows.presentation.screens.popular_tv_shows.PopularTvShowsFragmentDirections
import com.ru.movieshows.presentation.screens.sign_in.SignInFragmentDirections
import com.ru.movieshows.presentation.screens.splash.SplashFragmentDirections
import com.ru.movieshows.presentation.screens.tabs.TabsFragment
import com.ru.movieshows.presentation.screens.tabs.TabsFragmentDirections
import com.ru.movieshows.presentation.screens.top_rated_movies.TopRatedMoviesFragmentDirections
import com.ru.movieshows.presentation.screens.top_rated_tv_shows.TopRatedTvShowsFragmentDirections
import com.ru.movieshows.presentation.screens.tv_show_details.TvShowDetailsFragmentDirections
import com.ru.movieshows.presentation.screens.tv_show_search.TvShowSearchFragmentDirections
import com.ru.movieshows.presentation.screens.tvs.TvsFragmentDirections
import com.ru.movieshows.presentation.screens.uncoming_movies.UpcomingMoviesFragmentDirections
import com.ru.movieshows.presentation.viewmodel.main.AuthState
import com.ru.movieshows.presentation.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val rootNavController get() = rootNavController()
    private var _currentNavController: NavController? = null

    private val navHostFragment: NavHostFragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? NavHostFragment

    private val currentFragment: Fragment?
        get() {
            val navHostFragment = navHostFragment
            return navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = executeOnHandleBack(this)
    }

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentStarted(fm: FragmentManager, fragment: Fragment) {
            super.onFragmentStarted(fm, fragment)
            if(fragment is NavHostFragment) return
            onNavControllerActivated(fragment.findNavController())
        }

        override fun onFragmentCreated(fragmentManager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fragmentManager, fragment, savedInstanceState)
            if(fragment is NavHostFragment) return
            onNavControllerActivated(fragment.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setupNavigation()
        viewModel.state.observe(this, ::stateChanged)
        viewModel.authFailureState.observe(this, ::authFailureStateChanged)
    }

    override fun onStart() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
        super.onStart()
    }

    override fun onStop() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onStop()
    }

    private fun authFailureStateChanged(failure: AppFailure?) {
        if(failure == null) return
        val message = when (failure) {
            AppFailure.Pure -> R.string.there_was_a_problem_with_authorization
            AppFailure.Connection -> R.string.connect_to_the_internet
            is AppFailure.Message -> failure.value
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun stateChanged(state: AuthState) {
        when (state) {
            is AuthState.Authenticated -> navigateByAuthenticatedState()
            AuthState.NotAuthenticated -> navigateByNotAuthenticatedState()
            else -> setStartDestination()
        }
    }

    private fun isRootNestedRoute(): Boolean {
        val destinationId = _currentNavController?.currentDestination?.id
        return TabsFragment.tabsTopLevelFragment.any { it == destinationId }
    }

    private fun popToBackInCurrentNavControllerIfCan(): Boolean {
        return _currentNavController != null && _currentNavController?.popBackStack() == true
    }

    private fun executeOnHandleBack(onBackPressedCallback: OnBackPressedCallback) {
        when (isRootNestedRoute()) {
            true -> finish()
            false -> {
                if(popToBackInCurrentNavControllerIfCan()) return
                onBackPressedCallback.isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun rootNavController(): NavController {
        val fragmentContainer = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navHostFragment = fragmentContainer as NavHostFragment
        return navHostFragment.navController
    }

    private fun setupNavigation() {
        onNavControllerActivated(rootNavController)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun onNavControllerActivated(navController: NavController) {
        if(this._currentNavController == navController || navController == rootNavController) return
        this._currentNavController = navController
    }

    private fun setStartDestination() {
        val graph = rootNavController.navInflater.inflate(getAppNavigationGraphId())
        graph.setStartDestination(getSplashDestination())
        rootNavController.graph = graph
        _currentNavController = rootNavController
    }

    private fun navigateByNotAuthenticatedState() {
        when(rootNavController.currentDestination?.id) {
            R.id.splashFragment -> rootNavController.navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
            R.id.tabsFragment -> rootNavController.navigate(TabsFragmentDirections.actionTabsFragmentToSignInFragment4())
        }
    }

    private fun navigateByAuthenticatedState() {
        when(rootNavController.currentDestination?.id) {
            R.id.splashFragment -> rootNavController.navigate(SplashFragmentDirections.actionSplashFragmentToTabsFragment())
            R.id.signInFragment -> rootNavController.navigate(SignInFragmentDirections.actionSignInFragmentToTabsFragment3())
        }
    }

    private fun getAppNavigationGraphId(): Int = R.navigation.app_graph

    private fun getSplashDestination(): Int = R.id.splashFragment

    override fun navigateToPopularMovies() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.moviesFragment -> MoviesFragmentDirections.actionMoviesFragmentToPopularMoviesFragment2()
            else -> null
        } ?: return
        currentNavController.navigate(action)
    }

    override fun navigateToTopRatedMovies() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.moviesFragment -> MoviesFragmentDirections.actionMoviesFragmentToTopRatedMoviesFragment()
            else -> null
        } ?: return
        currentNavController.navigate(action)
    }

    override fun navigateToUpcomingMovies() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.moviesFragment -> MoviesFragmentDirections.actionMoviesFragmentToUpcomingMoviesFragment()
            else -> null
        } ?: return
        currentNavController.navigate(action)
    }

    override fun navigateToMovieDetails(id: Int) {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.moviesFragment -> MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
            R.id.airTvShowsFragment -> AirTvShowsFragmentDirections.actionAirTvShowsFragmentToTvShowDetailsFragment(id.toString())
            R.id.movieSearchFragment -> MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailsFragment(id)
            R.id.topRatedMoviesFragment -> TopRatedMoviesFragmentDirections.actionTopRatedMoviesFragmentToMovieDetailsFragment(id)
            R.id.popularMoviesFragment -> PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailsFragment(id)
            R.id.upcomingMoviesFragment -> UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailsFragment(id)
            R.id.movieDetailsFragment -> MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(id)
            else -> null
        } ?: return
        currentNavController.navigate(action)
    }

    override fun navigateToMovieSearch() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.moviesFragment -> MoviesFragmentDirections.actionMoviesFragmentToMovieSearchFragment()
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun pop() {
        _currentNavController?.popBackStack()
    }

    override fun navigateToTvShowsSearch() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.tvsFragment -> TvsFragmentDirections.actionTvsFragmentToTvShowSearchFragment()
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun navigateToTvShowDetails(id: Int) {
        val id = id.toString()
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.tvsFragment -> TvsFragmentDirections.actionTvsFragmentToTvShowDetailsFragment(id)
            R.id.popularTvShowsFragment -> PopularTvShowsFragmentDirections.actionPopularTvShowsFragmentToTvShowDetailsFragment(id)
            R.id.airTvShowsFragment -> AirTvShowsFragmentDirections.actionAirTvShowsFragmentToTvShowDetailsFragment(id)
            R.id.tvShowSearchFragment -> TvShowSearchFragmentDirections.actionTvShowSearchFragmentToTvShowDetailsFragment(id)
            R.id.topRatedTvShowsFragment -> TopRatedTvShowsFragmentDirections.actionTopRatedTvShowsFragmentToTvShowDetailsFragment(id)
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun navigateToAirTvShows() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.tvsFragment -> TvsFragmentDirections.actionTvsFragmentToAirTvShowsFragment()
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun navigateToTopRatedTvShows() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.tvsFragment -> TvsFragmentDirections.actionTvsFragmentToTopRatedTvShowsFragment()
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun navigateToPopularTvShows() {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.tvsFragment -> TvsFragmentDirections.actionTvsFragmentToPopularTvShowsFragment()
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun navigateToReviews(
        reviews: ArrayList<ReviewEntity>,
        movieId: Int
    ) {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.movieDetailsFragment -> MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieReviewsFragment(reviews.toTypedArray(), movieId)
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun navigateToVideo(video: VideoEntity) {
        val currentNavController = _currentNavController ?: return
        val action = when(currentNavController.currentDestination?.id) {
            R.id.movieDetailsFragment -> MovieDetailsFragmentDirections.actionMovieDetailsFragmentToVideoActivity(video)
            R.id.tvShowDetailsFragment -> TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToVideoActivity3(video)
            else -> null
        } ?: return
        _currentNavController?.navigate(action)
    }

    override fun getToolbar(): Toolbar? {
        val tabsFragment = currentFragment as? TabsFragment
        val view = tabsFragment?.view ?: return null
        val binding = FragmentTabsBinding.bind(view)
        return binding.tabsToolbar
    }

    override fun getBottomNavigationView(): BottomNavigationView? {
        val tabsFragment = currentFragment as? TabsFragment
        val view = tabsFragment?.view ?: return null
        val binding = FragmentTabsBinding.bind(view)
        return binding.bottomNavigationView
    }
}