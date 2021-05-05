package app.melon.home.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.home.MainActivity
import app.melon.home.anonymous.ForumFragment
import app.melon.home.discovery.DiscoveryFragment
import app.melon.home.nearby.NearbyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class HomeBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectMainActivity(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectDiscoveryFragment(): DiscoveryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectForumFragment(): ForumFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectNearbyFragment(): NearbyFragment
}