package app.melon.framework.di

import app.melon.base.initializer.AppInitializer
import app.melon.framework.helper.LocaleManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor


@Module(
    includes = [
        LocaleInitializerBinds::class
    ]
)
internal class LocaleModule {

    @Provides
    @IntoSet
    fun injectLocaleInterceptor(localeManager: LocaleManager): Interceptor {
        return localeManager.provideInterceptor()
    }
}

@Module
internal abstract class LocaleInitializerBinds {
    @Binds
    @IntoSet
    abstract fun bindLocaleInitializer(localeManager: LocaleManager): AppInitializer
}