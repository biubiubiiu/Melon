package app.melon.base.ui.transform

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation


class OverlayTransformation constructor(
    private val overlay: Drawable,
    private val resize: Boolean
) : Transformation {

    override fun key(): String = "${OverlayTransformation::class.java.name}-${overlay}"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val output = pool.get(input.width, input.height, input.config ?: Bitmap.Config.ARGB_8888)
        val overlayBitmap = drawable2Bitmap(overlay, output.width, output.height)
        output.applyCanvas {
            drawBitmap(input, 0f, 0f, paint)

            val marginLeft = (output.width - overlayBitmap.width) / 2f
            val marginTop = (output.height - overlayBitmap.height) / 2f
            drawBitmap(overlayBitmap, marginLeft, marginTop, null)
        }

        return output
    }

    private fun drawable2Bitmap(drawable: Drawable, width: Int, height: Int): Bitmap {

        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return drawable.bitmap
        }

        val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0 || resize) {
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is OverlayTransformation && overlay == other.overlay
    }

    override fun hashCode(): Int {
        return overlay.hashCode()
    }

    override fun toString(): String {
        return "OverlayTransformation(overlay=$overlay)"
    }
}