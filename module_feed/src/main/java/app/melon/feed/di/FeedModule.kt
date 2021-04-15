package app.melon.feed.di

import app.melon.base.scope.FragmentScope
import app.melon.feed.ui.CommonFeedFragment
import app.melon.feed.data.FeedApiService
import app.melon.feed.ui.FeedDetailFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit

@Module(
    includes = [
        FeedBuilder::class,
        RemoteServiceModule::class
    ]
)
class FeedModule

@Module
abstract class FeedBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectCommonFeedListFragment(): CommonFeedFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectFeedDetailFragment(): FeedDetailFragment
}

@Module
internal class RemoteServiceModule {

    @Provides
    fun provideFeedService(retrofit: Retrofit) = retrofit.create(FeedApiService::class.java)
}