package app.melon.base.initializer

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
