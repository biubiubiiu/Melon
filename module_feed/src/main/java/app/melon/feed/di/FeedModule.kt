package app.melon.feed.di

import app.melon.base.scope.FragmentScope
import app.melon.feed.anonymous.AnonymousFeedFragment
import app.melon.feed.anonymous.ui.ExploreFeedFragment
import app.melon.feed.anonymous.ui.SchoolFeedFragment
import app.melon.feed.anonymous.ui.TrendingFeedFragment
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
    abstract fun injectFeedFragment(): AnonymousFeedFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectSchoolFeedFragment(): SchoolFeedFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectExploreFeedFragment(): ExploreFeedFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectTrendingFeedFragment(): TrendingFeedFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectFeedDetailFragment(): FeedDetailFragment
}

@Module
internal class RemoteServiceModule {

    @Provides
    fun provideFeedService(retrofit: Retrofit) = retrofit.create(FeedApiService::class.java)
}