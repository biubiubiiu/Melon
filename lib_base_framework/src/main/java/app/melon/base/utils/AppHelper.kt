package app.melon.base.utils

import android.content.Context

object AppHelper {

    lateinit var applicationContext: Context
        private set

    fun init(context: Context) {
        applicationContext = context
    }
}