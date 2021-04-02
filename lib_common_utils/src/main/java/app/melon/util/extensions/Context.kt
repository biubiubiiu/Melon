package app.melon.util.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermissions(vararg permissions: String): Boolean {
    return permissions.firstOrNull { !hasPermission(it) } == null
}

fun Context.hideKeyboard() {
    val inputMethodManager = getSystemService<InputMethodManager>()!!
    inputMethodManager.hideSoftInputFromWindow((this as Activity).currentFocus?.windowToken, 0)
}

fun Context.showToast(@StringRes message: Int) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

val Context.connectivityManager: ConnectivityManager
    get() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.resolveTheme(@AttrRes attributeResId: Int): Int {
    val typedValue = TypedValue()
    return if (this.theme.resolveAttribute(attributeResId, typedValue, true)) {
        typedValue.data
    } else 0
}