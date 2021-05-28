package app.melon.base.network

import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkInterceptors @Inject constructor(
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>
) : Iterable<Interceptor> {
    override fun iterator(): Iterator<Interceptor> {
        return interceptors.iterator()
    }
}