package app.melon.base.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration constructor(
    private val spaceSize: Int,
    private val spaceAt: Int,
    private val firstSpaceSize: Int = spaceSize,
    private val lastSpaceSize: Int = spaceSize
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        val space = getSpaceSize(position, itemCount)

        if (spaceAt and LEFT > 0) outRect.left = space
        if (spaceAt and RIGHT > 0) outRect.right = space
        if (spaceAt and TOP > 0) outRect.top = space
        if (spaceAt and BOTTOM > 0) outRect.bottom = space
    }

    private fun getSpaceSize(position: Int, itemCount: Int): Int {
        return when (position) {
            0 -> firstSpaceSize
            itemCount - 1 -> lastSpaceSize
            else -> spaceSize
        }
    }

    companion object {
        const val LEFT = 0x1
        const val RIGHT = 0x2
        const val TOP = 0x4
        const val BOTTOM = 0x8
    }
}