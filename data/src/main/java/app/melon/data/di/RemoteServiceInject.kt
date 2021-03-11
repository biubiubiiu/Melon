package app.melon.data.di

import app.melon.data.services.FeedApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class RemoteServiceModule {
    @Provides
    fun provideFeedService(retrofit: Retrofit) = retrofit.create(FeedApiService::class.java)
}

@Module
class NetworkModule {

    private val ADDRESS = "10.16.81.181"
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}