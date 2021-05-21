package app.melon.feed.di

import app.melon.feed.FeedService
import app.melon.feed.IFeedService
import app.melon.feed.data.FeedApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(
    includes = [
        FeedBuilder::class,
        RemoteServiceModule::class,
        FeedServiceBinds::class
    ]
)
class FeedModule

@Module
internal abstract class FeedServiceBinds {
    @Binds
    abstract fun bindFeedService(service: FeedService): IFeedService
}

@Module
internal class RemoteServiceModule {

    @Provides
    fun provideFeedService(retrofit: Retrofit) = retrofit.create(FeedApi::class.java)
}