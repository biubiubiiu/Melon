package app.melon.feed.di

import app.melon.base.scope.FragmentScope
import app.melon.feed.anonymous.AnonymousFeedFragment
import app.melon.feed.anonymous.ui.ExploreFeedFragment
import app.melon.feed.anonymous.ui.SchoolFeedFragment
import app.melon.feed.anonymous.ui.TrendingFeedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        FeedBuilder::class,
        RemoteServiceModule::class
    ]
)
class FeedModule

@Module
abstract class FeedBuilder {

    // to add [FeedControllerDelegate] to dependency graph
    // couldn't find @AssistedModule yet, so doing this for now
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideDummyFragment(): DummyFragment

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
}

@Module
internal class RemoteServiceModule {

//    @Provides
//    fun provideFeedService(retrofit: Retrofit) = retrofit.create(FeedApiService::class.java)
}