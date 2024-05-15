package com.ru.movieshows.core.presentation.views

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(
    private val top: Int = 16,
    private val left: Int = 16,
    private val right: Int = 16,
    private val bottom: Int = 16,
    private val spanCount: Int = 3,
    private val halfPadding: Boolean = true,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val displayMetrics = view.resources.displayMetrics
        val adapter = parent.adapter

        val firstIndexInLine = 0
        val lastIndexInLine = spanCount - 1

        val index = itemPosition % spanCount

        val rightValue = if(halfPadding) (if(lastIndexInLine == index) right else right / 2) else right
        val leftValue = if(halfPadding) (if(firstIndexInLine == index) left else left / 2) else left

        val topPadding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top.toFloat(), displayMetrics).toInt()
        val rightPadding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightValue.toFloat(), displayMetrics).toInt()
        val leftPadding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftValue.toFloat(), displayMetrics).toInt()
        val bottomPadding: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottom.toFloat(), displayMetrics).toInt()

        if (itemPosition == RecyclerView.NO_POSITION) return

        outRect.top = topPadding
        outRect.right = rightPadding
        outRect.left = leftPadding

        if (adapter != null && itemPosition == adapter.itemCount - 1) {
            outRect.bottom = bottomPadding
        }
    }

}