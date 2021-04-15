package app.melon.base.di

import app.melon.util.network.TokenManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    private val ADDRESS = "10.16.80.28"
    private val PORT = "3000"
    private val API_HOST = "$ADDRESS:$PORT"
    private val BASE_URL = "http://$API_HOST/"

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenManager.provideInterceptor())
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}