package app.melon

import android.content.Context
import androidx.multidex.MultiDex
import app.melon.di.DaggerAppComponent
import app.melon.initializers.ThemeInitializer
import app.melon.settings.theme.ThemePreferences
import app.melon.util.AppHelper
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject


class MelonApplication : DaggerApplication() {

    @Inject internal lateinit var themeInitializer: ThemeInitializer

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()

        AppHelper.init(this)
        themeInitializer.init()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}