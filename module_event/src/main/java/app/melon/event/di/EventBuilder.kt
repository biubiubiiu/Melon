package app.melon.event.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.event.EventDetailActivity
import app.melon.event.MyEventsActivity
import app.melon.event.NearbyEventsActivity
import app.melon.event.mine.JoiningEventsFragment
import app.melon.event.mine.OrganisedEventsFragment
import app.melon.event.nearby.NearbyEventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class EventBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectEventActivity(): NearbyEventsActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectEventDetailActivity(): EventDetailActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectMyEventsActivity(): MyEventsActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectEventListFragment(): NearbyEventsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectOrganisedEventsFragment(): OrganisedEventsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectJoiningEventsFragment(): JoiningEventsFragment
}