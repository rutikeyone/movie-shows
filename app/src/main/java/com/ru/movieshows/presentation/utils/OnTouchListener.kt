package com.ru.movieshows.presentation.utils

import android.annotation.SuppressLint
import android.text.Layout
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class OnTouchListener : View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val widget = v as? TextView ?: return false
        val text: Any = widget.text
        if (text is Spanned) {
            val action = event.action
            val isActionUpOrDown = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN
            if (isActionUpOrDown) {
                val layout: Layout = widget.layout
                val (x, y) = calculateXY(event, widget)
                val line: Int = layout.getLineForVertical(y)
                val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())
                val link = text.getSpans(off, off, ClickableSpan::class.java)
                val isOnClickLink = link.isNotEmpty() && action == MotionEvent.ACTION_DOWN
                if (isOnClickLink) {
                    link[0].onClick(widget)
                    return true
                }
            }
        }
        return false
    }

    private fun calculateXY(event: MotionEvent, widget: TextView): Pair<Int, Int> {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop
        x += widget.scrollX
        y += widget.scrollY
        return Pair(x, y)
    }
}