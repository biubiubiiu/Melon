package app.melon.util.extensions

import android.view.View
import android.view.ViewTreeObserver

inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            removeOnGlobalLayoutListener(this)
            f()
        }
    })
}