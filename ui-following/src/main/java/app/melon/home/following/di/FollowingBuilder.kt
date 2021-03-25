package app.melon.home.following.di

import app.melon.base.scope.FragmentScope
import app.melon.home.following.ui.FollowFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FollowingBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun followFragment(): FollowFragment
}