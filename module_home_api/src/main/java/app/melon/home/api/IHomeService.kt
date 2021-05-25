package app.melon.home.api

import android.content.Context

interface IHomeService {
    fun routeToHomepage(context: Context, clearTask: Boolean)
}