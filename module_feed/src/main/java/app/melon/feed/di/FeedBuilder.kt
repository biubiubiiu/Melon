package app.melon.feed.di

import app.melon.base.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FeedBuilder {

    // to add [FeedControllerDelegate] to dependency graph
    // couldn't find @AssistedModule yet, so doing this for now
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideDummyFragment(): DummyFragment
}