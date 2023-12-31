package com.ru.movieshows.presentation.adapters

import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(
    private val paddingsInDips: Float = 16F,
    private val metrics: DisplayMetrics
) : RecyclerView.ItemDecoration() {
    private val paddings: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingsInDips, metrics).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {

        val itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }
        outRect.top = paddings
        outRect.left = paddings
        outRect.right = paddings

        val adapter = parent.adapter
        if (adapter != null && itemPosition == adapter.itemCount - 1) {
            outRect.bottom = paddings
        }
    }

}