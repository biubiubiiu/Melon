package app.melon

import android.content.Context
import androidx.multidex.MultiDex
import app.melon.di.DaggerAppComponent
import com.airbnb.mvrx.Mavericks
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MelonApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
        Router.init(DefaultRootUriHandler(applicationContext))
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}