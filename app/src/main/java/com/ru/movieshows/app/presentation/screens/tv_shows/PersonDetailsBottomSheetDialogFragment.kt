package com.ru.movieshows.app.presentation.screens.tv_shows

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ru.movieshows.app.R
import com.ru.movieshows.app.presentation.screens.BaseBottomSheetDialogFragment
import com.ru.movieshows.app.presentation.viewmodel.tv_shows.PersonDetailsViewModel
import com.ru.movieshows.app.utils.viewBinding
import com.ru.movieshows.app.utils.viewModelCreator
import com.ru.movieshows.app.databinding.FragmentPersonDetailsBottomSheetDialogBinding
import com.ru.movieshows.sources.people.entities.PersonEntity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailsBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    @Inject
    lateinit var factory: PersonDetailsViewModel.Factory

    private val viewModel by viewModelCreator {
        factory.create(
            initialPerson = person,
        )
    }

    private val person: PersonEntity by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PERSON_ARG, PersonEntity::class.java)
        } else {
            arguments?.getParcelable(PERSON_ARG)
        } ?: throw IllegalStateException("The season argument must be passed")
    }

    private val binding by viewBinding<FragmentPersonDetailsBottomSheetDialogBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_person_details_bottom_sheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.person.observe(viewLifecycleOwner, ::configureUI)
        viewModel.update()
    }

    private fun configureUI(person: PersonEntity?) {
        if(person == null) return
        configureImageUI(person)
        configureHeaderUI(person)
        configureDateOfBirthUI(person)
        configureDateOfDeathUI(person)
        configureBiographyUI(person)
        configurePlaceOfBirthUI(person)
    }

    private fun configurePlaceOfBirthUI(person: PersonEntity) {
        val placeOfBirth = person.placeOfBirth
        if(!placeOfBirth.isNullOrEmpty()) {
            binding.placeOfBirthTextView.text = placeOfBirth
            binding.placeOfBirthTextView.isVisible = true
            binding.placeOfBirthHeaderTextView.isVisible = true
        } else {
            binding.placeOfBirthTextView.isVisible = false
            binding.placeOfBirthHeaderTextView.isVisible = false
        }
    }

    private fun configureBiographyUI(person: PersonEntity) {
        val biography = person.biography
        if(!biography.isNullOrEmpty()) {
            binding.biographyTextView.text = biography
            binding.biographyHeaderTextView.isVisible = true
            binding.biographyTextView.isVisible = true
        } else {
            binding.biographyHeaderTextView.isVisible = false
            binding.biographyTextView.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureDateOfDeathUI(person: PersonEntity) {
        val deathday = person.deathday
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")
        if(deathday != null) {
            val value = dateFormatter.format(deathday)
            binding.dateDeathTextView.text = value
        } else {
            binding.dateDeathHeaderTextView.isVisible = false
            binding.dateDeathTextView.isVisible = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun configureDateOfBirthUI(person: PersonEntity) {
        val birthday = person.birthday
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")
        if(birthday != null) {
            val value = dateFormatter.format(birthday)
            binding.dateBirthdayTextView.text = value
        } else {
            binding.dateBirthdayHeaderTextView.isVisible = false
            binding.dateBirthdayTextView.isVisible = false
        }
    }

    private fun configureHeaderUI(person: PersonEntity) = with(binding) {
        val name = person.name
        if(!name.isNullOrEmpty()) {
            seasonName.text = name
            seasonName.isVisible = true
        } else {
            seasonName.isVisible = false
        }
    }

    private fun configureImageUI(person: PersonEntity) = with(binding.poster) {
        val photo = person.profilePath
        if(!photo.isNullOrEmpty()) {
            Glide
                .with(this)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        } else {
            Glide
                .with(context)
                .load(R.drawable.poster_placeholder_bg)
                .placeholder(R.drawable.poster_placeholder_bg)
                .error(R.drawable.poster_placeholder_bg)
                .centerCrop()
                .into(this)
        }
    }

    companion object {
        fun newInstance(person: PersonEntity): PersonDetailsBottomSheetDialogFragment {
            val arguments = Bundle()
            arguments.putParcelable(PERSON_ARG, person)
            val fragment = PersonDetailsBottomSheetDialogFragment()
            fragment.arguments = arguments
            return fragment
        }

        private const val PERSON_ARG = "personArgument"
    }

}