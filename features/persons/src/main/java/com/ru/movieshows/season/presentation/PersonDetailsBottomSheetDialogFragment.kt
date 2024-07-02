package com.ru.movieshows.season.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.core.presentation.viewModelCreator
import com.ru.movieshows.core.presentation.views.observe
import com.ru.movieshows.season.domain.entities.Person
import com.ru.movieshows.seasons.R
import com.ru.movieshows.seasons.databinding.FragmentPersonDetailsBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: PersonDetailsViewModel.Factory

    private val binding by viewBinding<FragmentPersonDetailsBottomSheetDialogBinding>()

    private val personId: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getString(PERSON_ID_ARG)
        } else {
            arguments?.getString(PERSON_ID_ARG)
        } ?: throw IllegalStateException("The season Id argument must be passed")
    }

    private val viewModel by viewModelCreator {
        factory.create(personId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(
            activity,
            com.ru.movieshows.core.theme.R.style.Base_Theme_MovieShows
        )
        return inflater.cloneInContext(contextThemeWrapper)
            .inflate(R.layout.fragment_person_details_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val minHeight =
            resources.getDimensionPixelOffset(com.ru.movieshows.core.presentation.R.dimen.dp_240)

        with(binding.root) {
            setTryAgainListener { viewModel.toTryAgain() }
            this.minHeight = minHeight
            this.layoutParams.height = minHeight
        }


        binding.root.observe(viewLifecycleOwner, viewModel.loadScreenStateLiveValue) {
            setupViews(it)
        }

        viewModel.updatePerson()
    }

    private fun setupViews(person: Person?) {

        with(binding.root) {
            this.minHeight = ConstraintLayout.LayoutParams.WRAP_CONTENT
            this.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }

        setupImageView(person)
        setupHeaderView(person)
        setupDateOfBirthView(person)
        setupDateOfDeathView(person)
        setupBiographyView(person)
        setupPlaceOfBirthView(person)
    }

    private fun setupHeaderView(person: Person?) {
        val personName = person?.name

        with(binding) {
            if (!personName.isNullOrEmpty()) {
                seasonNameTextView.text = personName
                seasonNameTextView.isVisible = true
            } else {
                seasonNameTextView.isVisible = false
            }
        }
    }

    private fun setupImageView(person: Person?) {
        with(binding.personImageView) {
            val loadProfilePath = person?.profilePath

            Glide.with(this).load(loadProfilePath).centerCrop()
                .placeholder(com.ru.movieshows.core.presentation.R.drawable.core_presentation_bg_poster_placeholder)
                .error(com.ru.movieshows.core.presentation.R.drawable.core_presentation_bg_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade()).into(this)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupDateOfBirthView(person: Person?) {
        val personBirthday = person?.birthday
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        if (personBirthday != null) {
            val value = dateFormatter.format(personBirthday)
            binding.dateBirthdayTextView.text = value
        } else {
            binding.dateBirthdayHeaderTextView.isVisible = false
            binding.dateBirthdayTextView.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupDateOfDeathView(person: Person?) {
        val personDeathday = person?.deathday
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        if (personDeathday != null) {
            val value = dateFormatter.format(personDeathday)
            binding.dateDeathTextView.text = value
        } else {
            binding.dateDeathHeaderTextView.isVisible = false
            binding.dateDeathTextView.isVisible = false
        }
    }

    private fun setupBiographyView(person: Person?) {
        val personBiography = person?.biography

        if (!personBiography.isNullOrEmpty()) {
            binding.biographyTextView.text = personBiography
            binding.biographyHeaderTextView.isVisible = true
            binding.biographyTextView.isVisible = true
        } else {
            binding.biographyHeaderTextView.isVisible = false
            binding.biographyTextView.isVisible = false
        }
    }

    private fun setupPlaceOfBirthView(person: Person?) {
        val placeOfBirth = person?.placeOfBirth

        if (!placeOfBirth.isNullOrEmpty()) {
            binding.placeOfBirthTextView.text = placeOfBirth
            binding.placeOfBirthTextView.isVisible = true
            binding.placeOfBirthHeaderTextView.isVisible = true
        } else {
            binding.placeOfBirthTextView.isVisible = false
            binding.placeOfBirthHeaderTextView.isVisible = false
        }
    }

    companion object {

        fun newInstance(id: String): PersonDetailsBottomSheetDialogFragment {
            val arguments = Bundle()
            arguments.putString(PERSON_ID_ARG, id)
            val fragment = PersonDetailsBottomSheetDialogFragment()
            fragment.arguments = arguments
            return fragment
        }

        private const val PERSON_ID_ARG = "personIdArgument"
    }

}