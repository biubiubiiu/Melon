package app.melon.event.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.event.NearbyEventsActivity
import app.melon.event.nearby.NearbyEventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class EventBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectEventActivity(): NearbyEventsActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectEventListFragment(): NearbyEventsFragment
}