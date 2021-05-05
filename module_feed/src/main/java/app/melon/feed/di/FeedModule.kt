package app.melon.feed.di

import app.melon.feed.data.FeedApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(
    includes = [
        FeedBuilder::class,
        RemoteServiceModule::class
    ]
)
class FeedModule

@Module
internal class RemoteServiceModule {

    @Provides
    fun provideFeedService(retrofit: Retrofit) = retrofit.create(FeedApiService::class.java)
}