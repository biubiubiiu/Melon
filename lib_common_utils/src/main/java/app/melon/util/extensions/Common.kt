package app.melon.util.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import app.melon.util.AppHelper
import java.lang.ref.WeakReference

val Float.dp: Float
    get() = AppHelper.applicationContext.resources.displayMetrics.density * this + 0.5f

val Float.dpInt: Int
    get() = this.dp.toInt()

val Float.px: Float
    get() = this / AppHelper.applicationContext.resources.displayMetrics.density + 0.5f

val Int.px: Float
    get() = this.toFloat().px

val Int.dp: Float
    get() = this.toFloat().dp

val Int.dpInt: Int
    get() = this.dp.toInt()

val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        AppHelper.applicationContext.resources.displayMetrics
    )

val Float.spInt: Int
    get() = this.sp.toInt()

val Int.sp: Float
    get() = this.toFloat().sp

val Int.spInt: Int
    get() = this.toFloat().sp.toInt()

val Int.toColor: Int
    get() = ContextCompat.getColor(AppHelper.applicationContext, this)

fun <T> T.weakRef(): WeakReference<T> = WeakReference(this)

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Any.isAnyOf(vararg args: Any): Boolean {
    return args.any { it == this }
}

fun Any.equalsAll(vararg args: Any): Boolean {
    return args.all { it == this }
}

fun getResourceString(@StringRes id: Int, vararg formatArgs: Any?): String {
    return AppHelper.applicationContext.getString(id, *formatArgs)
}

fun getResourceColor(@ColorRes id: Int): Int {
    return AppHelper.applicationContext.getColorCompat(id)
}

fun getResourceQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
    return AppHelper.applicationContext.resources.getQuantityString(id, quantity, *formatArgs)
}

private val uiHandler = Handler(Looper.getMainLooper())

@JvmOverloads
fun runOnUIThread(delayMs: Long = 0, action: () -> Unit) {
    if (delayMs <= 0) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action()
        } else {
            uiHandler.post(action)
        }
    } else {
        uiHandler.postDelayed(action, delayMs)
    }
}

inline fun Menu.filter(predicate: (MenuItem) -> Boolean): List<MenuItem> {
    val destination = mutableListOf<MenuItem>()
    this.forEach {
        if (predicate(it)) destination.add(it)
    }
    return destination
}