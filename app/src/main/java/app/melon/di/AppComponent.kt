package app.melon.di

import app.melon.MelonApplication
import app.melon.account.di.AccountModule
import app.melon.base.di.NetworkModule
import app.melon.base.scope.ApplicationScope
import app.melon.comment.di.CommentModule
import app.melon.composer.di.ComposerModule
import app.melon.data.di.DatabaseModule
import app.melon.event.di.EventModule
import app.melon.feed.di.FeedModule
import app.melon.framework.di.FrameworkModule
import app.melon.group.di.GroupBuilder
import app.melon.home.di.HomeModule
import app.melon.im.di.IMModule
import app.melon.poi.di.PoiModule
import app.melon.settings.di.SettingsModule
import app.melon.splash.di.SplashBuilder
import app.melon.user.di.UserModule
import app.melon.util.di.UtilModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * The application's root component
 */
@Singleton
@ApplicationScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        SplashBuilder::class,
        HomeModule::class,
        ComposerModule::class,
        UserModule::class,
        IMModule::class,
        EventModule::class,
        AccountModule::class,
        FeedModule::class,
        CommentModule::class,
        PoiModule::class,
        GroupBuilder::class,
        SettingsModule::class,
        UtilModule::class,
        FrameworkModule::class
    ]
)
interface AppComponent : AndroidInjector<MelonApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: MelonApplication): AppComponent
    }
}