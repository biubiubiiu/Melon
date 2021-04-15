package app.melon.home.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.home.MainActivity
import app.melon.home.anonymous.AnonymousForumFragment
import app.melon.home.discovery.DiscoveryFragment
import app.melon.home.nearby.NearbyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class HomeBuilder {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectHomeActivity(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun discoverFragment(): DiscoveryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun nearbyFragment(): NearbyFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun forumFragment(): AnonymousForumFragment
}