package app.melon.base.ui.extensions

import com.github.chrisbanes.photoview.PhotoView


fun PhotoView.resetScale(animate: Boolean) {
    setScale(minimumScale, animate)
}