package app.melon.util.storage

import android.graphics.drawable.Drawable
import app.melon.util.graphics.GraphicsUtil

object StorageUtil {

    fun saveDrawableToGallery(drawable: Drawable) {
        val bitmap = GraphicsUtil.drawable2Bitmap(drawable)

    }
}