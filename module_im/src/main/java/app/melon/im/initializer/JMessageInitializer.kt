package app.melon.im.initializer

import android.app.Application
import app.melon.base.initializer.AppInitializer
import app.melon.im.BuildConfig
import cn.jpush.im.android.api.JMessageClient
import javax.inject.Inject


internal class JMessageInitializer @Inject constructor() : AppInitializer {

    override fun init(application: Application) {
        JMessageClient.setDebugMode(BuildConfig.DEBUG)
        JMessageClient.init(application)
    }
}