package app.melon.base.di

import app.melon.base.network.NetworkInterceptors
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    private companion object {

        init {
            System.loadLibrary("crypto-data")
        }

        @JvmStatic
        external fun baseUrlFromJNI(): String
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrlFromJNI())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptors: NetworkInterceptors): OkHttpClient {
        return OkHttpClient.Builder()
            .also { builder ->
                interceptors.forEach {
                    builder.addInterceptor(it)
                }
            }
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}