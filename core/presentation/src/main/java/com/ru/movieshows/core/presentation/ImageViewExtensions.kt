package com.ru.movieshows.core.presentation

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.loadUrl(
    url: String,
    @DrawableRes placeholderResource: Int? = null,
    @DrawableRes errorResource: Int? = null,
) {
    Glide
        .with(context)
        .load(url)
        .placeholder(placeholderResource ?: R.drawable.core_presentation_poster_placeholder_bg)
        .error(errorResource ?: R.drawable.core_presentation_poster_placeholder_bg)
        .centerCrop()
        .into(this)
}

fun ImageView.loadResource(
    @DrawableRes id: Int,
    @DrawableRes placeholderResource: Int? = null,
    @DrawableRes errorResource: Int? = null,
) {
    Glide
        .with(context)
        .load(id)
        .placeholder(placeholderResource ?: R.drawable.core_presentation_poster_placeholder_bg)
        .error(errorResource ?: R.drawable.core_presentation_poster_placeholder_bg)
        .centerCrop()
        .into(this)
}
