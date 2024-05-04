package com.ru.movieshows.tv_shows.presentation.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ru.movieshows.core.presentation.viewBinding
import com.ru.movieshows.tv_shows.domain.entities.Person
import com.ru.movieshows.tvshows.R
import com.ru.movieshows.tvshows.databinding.FragmentPersonDetailsBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class PersonDetailsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding<FragmentPersonDetailsBottomSheetDialogBinding>()

    private val person: Person by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PERSON_ARG, Person::class.java)
        } else {
            arguments?.getParcelable(PERSON_ARG)
        } ?: throw IllegalStateException("The season argument must be passed")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? =
        inflater.inflate(R.layout.fragment_person_details_bottom_sheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(person)
    }

    private fun setupViews(person: Person?) {
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

            Glide
                .with(this)
                .load(loadProfilePath)
                .centerCrop()
                .placeholder(R.drawable.bg_poster_placeholder)
                .error(R.drawable.bg_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupDateOfBirthView(person: Person?) {
        val personBirthday = person?.birthday
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")

        if(personBirthday != null) {
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

        if(personDeathday != null) {
            val value = dateFormatter.format(personDeathday)
            binding.dateDeathTextView.text = value
        } else {
            binding.dateDeathHeaderTextView.isVisible = false
            binding.dateDeathTextView.isVisible = false
        }
    }

    private fun setupBiographyView(person: Person?) {
        val personBiography = person?.biography

        if(!personBiography.isNullOrEmpty()) {
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

        if(!placeOfBirth.isNullOrEmpty()) {
            binding.placeOfBirthTextView.text = placeOfBirth
            binding.placeOfBirthTextView.isVisible = true
            binding.placeOfBirthHeaderTextView.isVisible = true
        } else {
            binding.placeOfBirthTextView.isVisible = false
            binding.placeOfBirthHeaderTextView.isVisible = false
        }
    }

    companion object {

        fun newInstance(person: Person): PersonDetailsBottomSheetDialogFragment {
            val arguments = Bundle()
            arguments.putParcelable(PERSON_ARG, person)
            val fragment = PersonDetailsBottomSheetDialogFragment()
            fragment.arguments = arguments
            return fragment
        }

        private const val PERSON_ARG = "personArgument"
    }

}