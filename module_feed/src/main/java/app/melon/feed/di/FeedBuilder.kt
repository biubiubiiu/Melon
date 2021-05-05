package app.melon.feed.di

import app.melon.base.scope.FragmentScope
import app.melon.feed.PostFeedService
import app.melon.feed.ui.CommonFeedFragment
import app.melon.feed.ui.FeedDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FeedBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectCommonFeedListFragment(): CommonFeedFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectFeedDetailFragment(): FeedDetailFragment

    @ContributesAndroidInjector
    internal abstract fun injectPostFeedService(): PostFeedService
}