package com.litcove.litcove.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CenteredGridItemDecoration(totalWidth: Int, private val numColumns: Int, itemWidth: Int) : RecyclerView.ItemDecoration() {
    private val totalSpacing = totalWidth - numColumns * itemWidth
    private val halfSpacing = totalSpacing / (numColumns + 1)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % numColumns

        outRect.left = halfSpacing - column * totalSpacing / numColumns
        outRect.right = (column + 1) * totalSpacing / numColumns - halfSpacing
        if (position < numColumns) {
            outRect.top = halfSpacing
        }
        outRect.bottom = halfSpacing
    }
}