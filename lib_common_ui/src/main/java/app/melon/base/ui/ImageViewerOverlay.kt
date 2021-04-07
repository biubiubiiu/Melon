package app.melon.base.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class ImageViewerOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mUrl: String = ""
    var onSaveClick: (String) -> Unit = {}

    init {
        fitsSystemWindows = true
        val view = View.inflate(context, R.layout.view_image_viewer_overlay, this)
        view.findViewById<View>(R.id.save_button).setOnClickListener {
            onSaveClick(mUrl)
        }
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update(url: String) {
        mUrl = url
    }
}