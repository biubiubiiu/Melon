package app.melon.util.network

import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor() {

    private var token: String? = null

    fun updateToken(token: String) {
        this.token = token
    }

    fun clearToken() {
        this.token = null
    }

    fun provideInterceptor() = Interceptor { chain ->
        val original = chain.request()

        // Request customization: add request headers
        val token = this.token
        val request = if (token == null) {
            original
        } else {
            original.newBuilder().header(AUTH_HEADER, token).build()
        }
        chain.proceed(request)
    }

    private companion object {
        const val AUTH_HEADER = "Authorization"
    }
}