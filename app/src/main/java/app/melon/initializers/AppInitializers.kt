package app.melon.initializers

import android.app.Application
import app.melon.base.initializer.AppInitializer
import javax.inject.Inject

class  AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) {
    fun init(application: Application) {
        initializers.forEach {
            it.init(application)
        }
    }
}
