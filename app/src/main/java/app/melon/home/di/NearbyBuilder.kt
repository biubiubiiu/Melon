package app.melon.home.di

import app.melon.base.scope.FragmentScope
import app.melon.home.nearby.NearbyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NearbyBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun nearbyFragment(): NearbyFragment
}