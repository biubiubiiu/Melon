package app.melon.di

import app.melon.MelonApplication
import app.melon.account.di.AccountModule
import app.melon.base.scope.ApplicationScope
import app.melon.data.di.DatabaseModule
import app.melon.data.di.RemoteServiceModule
import app.melon.feed.di.FeedBuilder
import app.melon.home.di.HomeBuilder
import app.melon.user.di.UserModule
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
        RemoteServiceModule::class,
        HomeBuilder::class,
        UserModule::class,
        AccountModule::class,
        FeedBuilder::class
    ]
)
interface AppComponent : AndroidInjector<MelonApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: MelonApplication): AppComponent
    }
}