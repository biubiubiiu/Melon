package app.melon.base.ui.extensions

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

val Context.activityContext: AppCompatActivity?
    get() = getActivity(this)

fun getActivity(context: Context): AppCompatActivity? {
    if (context is ContextWrapper) {
        return if (context is AppCompatActivity) {
            context
        } else {
            getActivity(context.baseContext)
        }
    }
    return null
}