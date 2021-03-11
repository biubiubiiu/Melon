package app.melon.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
//import com.google.android.material.snackbar.Snackbar
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.getSystemService
import androidx.core.graphics.toColorInt

private const val TAG = "TypeExtensions"

fun Fragment.startActivity(cls: Class<*>) {
    startActivity(Intent(context, cls))
}

fun String.urlIntent(): Intent =
    Intent(Intent.ACTION_VIEW).setData(Uri.parse(this))

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layout, this, attachToRoot)

fun String.hasPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, this) == PackageManager.PERMISSION_GRANTED

fun TextView.setDrawableLeft(@DrawableRes drawableRes: Int, activity: Activity) {
    val drawable = VectorDrawableCompat.create(resources, drawableRes, activity.theme)
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

//fun View.showSnackbarLong(@StringRes message: Int) =
//    showSnackbarLong(resources.getString(message))
//
//fun View.showSnackbarLong(message: String) =
//    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()

fun Context.showToast(@StringRes message: Int) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.setVisibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.onThrottledClick(
    throttleDelay: Long = 500L,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

inline fun <reified T> flatten(vararg lists: List<T>?) = lists.flatMap { it ?: emptyList() }

fun Float.lerp(other: Float, amount: Float): Float = this + amount * (other - this)

fun Float.sqrt() = Math.sqrt(this.toDouble()).toFloat()

fun View.getText(@StringRes res: Int) = this.resources.getText(res)
operator fun Boolean.inc() = !this

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

//fun Vibrator.vibrateCompat(millis: Long) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
//    } else {
//        @Suppress("DEPRECATION")
//        vibrate(millis)
//    }
//}

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
        Log.w(TAG, "Unable to parse $bgColor.")
        Color.WHITE
    }
}

fun Context.hideKeyboard() {
    val inputMethodManager = getSystemService<InputMethodManager>()!!
    inputMethodManager.hideSoftInputFromWindow((this as Activity).currentFocus?.windowToken, 0)
}

val Context.connectivityManager: ConnectivityManager
    get() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager