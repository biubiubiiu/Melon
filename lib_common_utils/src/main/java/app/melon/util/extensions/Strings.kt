package app.melon.util.extensions

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.graphics.toColorInt

inline fun String.ifNotBlank(block: (String) -> Unit) {
    if (this.isNotBlank()) {
        block(this)
    }
}

inline fun String.ifNotEmpty(block: (String) -> Unit) {
    if (this.isNotEmpty()) {
        block(this)
    }
}

fun String.urlIntent(): Intent =
    Intent(Intent.ACTION_VIEW).setData(Uri.parse(this))

@ColorInt
fun String?.toColorIntSafe(): Int {
    var bgColor = this ?: "#ffffff"
    bgColor = if (bgColor.startsWith("#")) bgColor else "#$bgColor"

    return try {
        when (bgColor.length) {
            0 -> "#ffffff"
            4 -> "#%c%c%c%c%c%c".format(
                bgColor[1], bgColor[1],
                bgColor[2], bgColor[2],
                bgColor[3], bgColor[3]
            )
            5 -> "#%c%c%c%c%c%c%c%c".format(
                bgColor[1], bgColor[1],
                bgColor[2], bgColor[2],
                bgColor[3], bgColor[3],
                bgColor[4], bgColor[4]
            )
            else -> bgColor
        }.toColorInt()
    } catch (e: IllegalArgumentException) {
        Log.w("Melon", "Unable to parse $bgColor.")
        Color.WHITE
    }
}