package com.litcove.litcove.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        if (position == 0) {
            outRect.top = spacing
        }
        if (position == itemCount - 1) {
            outRect.bottom = spacing
        }
    }
}