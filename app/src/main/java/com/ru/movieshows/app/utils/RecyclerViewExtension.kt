package com.ru.movieshows.app.utils

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.R
import com.ru.movieshows.app.presentation.adapters.ItemDecoration

fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (i in itemDecorationCount - 1 downTo 0) {
            removeItemDecorationAt(i)
        }
    }
}

fun RecyclerView.createItemDecoration(
    itemDecoration: Int = DividerItemDecoration.HORIZONTAL,
    @DrawableRes drawable: Int = R.drawable.divider,
): RecyclerView.ItemDecoration {
    val context = this.context
    val dividerDrawable = ContextCompat.getDrawable(context, drawable)
    val decoration = DividerItemDecoration(context, itemDecoration).apply {
        dividerDrawable?.let { this.setDrawable(it) }
    }
    return decoration
}

fun RecyclerView.applyDecoration(
    itemDecoration: RecyclerView.ItemDecoration = createItemDecoration(),
) {
    this.clearDecorations()
    addItemDecoration(itemDecoration)
}