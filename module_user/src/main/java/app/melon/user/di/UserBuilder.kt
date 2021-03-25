package app.melon.user.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.user.ui.posts.UserPostsFragment
import app.melon.user.UserProfileActivity
import app.melon.user.ui.UserProfileContainerFragment
import app.melon.user.ui.detail.UserProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun provideActivity(): UserProfileActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideContainerFragment(): UserProfileContainerFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): UserProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providePostsFragment(): UserPostsFragment
}