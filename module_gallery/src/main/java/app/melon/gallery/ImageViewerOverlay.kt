package app.melon.gallery

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import app.melon.base.ui.R
import app.melon.gallery.databinding.ViewImageViewerOverlayBinding


class ImageViewerOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var onBackClick: (() -> Unit)? = null
    var onSaveClick: ((String) -> Unit)? = null

    private val binding = ViewImageViewerOverlayBinding.inflate(LayoutInflater.from(context), this)

    private val backButton get() = binding.back
    private val moreButton get() = binding.more

    init {
        fitsSystemWindows = true
        setBackgroundColor(Color.TRANSPARENT)

        backButton.setOnClickListener { onBackClick?.invoke() }
        moreButton.setOnClickListener { showMenu(it, R.menu.gallery_popup_menu) }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.save) {
                onSaveClick?.invoke("")
            }
            true
        }
        popup.show()
    }
}