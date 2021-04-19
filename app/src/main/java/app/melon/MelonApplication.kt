package app.melon

import android.content.Context
import androidx.multidex.MultiDex
import app.melon.di.DaggerAppComponent
import app.melon.util.AppHelper
import com.airbnb.mvrx.Mavericks
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class MelonApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)

        AppHelper.init(this)
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}