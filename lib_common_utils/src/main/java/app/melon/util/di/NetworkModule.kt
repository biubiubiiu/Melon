package app.melon.util.di

import app.melon.util.network.TokenManager
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor


@Module
internal class NetworkModule {

    @Provides
    @IntoSet
    internal fun injectTokenInterceptor(tokenManager: TokenManager): Interceptor {
        return tokenManager.provideInterceptor()
    }
}