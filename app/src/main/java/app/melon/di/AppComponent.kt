package app.melon.di

import app.melon.MelonApplication
import app.melon.account.di.AccountModule
import app.melon.account.di.StorageModule
import app.melon.base.di.NetworkModule
import app.melon.base.scope.ApplicationScope
import app.melon.comment.di.CommentModule
import app.melon.data.di.DatabaseModule
import app.melon.feed.di.FeedModule
import app.melon.group.di.GroupBuilder
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
        NetworkModule::class,
        HomeBuilder::class,
        UserModule::class,
        AccountModule::class,
        FeedModule::class,
        CommentModule::class,
        GroupBuilder::class,
        StorageModule::class,
        AppSubComponents::class
    ]
)
interface AppComponent : AndroidInjector<MelonApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: MelonApplication): AppComponent
    }
}