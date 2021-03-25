package app.melon.home.di

import app.melon.base.scope.FragmentScope
import app.melon.home.discovery.DiscoveryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DiscoverBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun discoverFragment(): DiscoveryFragment
}