package app.melon.home.di

import app.melon.base.scope.ActivityScope
import app.melon.home.following.di.FollowingBuilder
import app.melon.home.recommend.di.RecommendBuilder
import app.melon.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class HomeBuilder {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            DiscoverBuilder::class,
            RecommendBuilder::class,
            FollowingBuilder::class,
            NearbyBuilder::class
        ]
    )
    internal abstract fun homeActivity(): MainActivity
}