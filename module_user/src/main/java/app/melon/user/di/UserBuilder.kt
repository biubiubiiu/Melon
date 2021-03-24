package app.melon.user.di

import app.melon.user.UserPostsFragment
import app.melon.user.UserProfileActivity
import app.melon.user.UserProfileContainerFragment
import app.melon.user.UserProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserBuilder {
    @ContributesAndroidInjector
    abstract fun provideActivity(): UserProfileActivity

    @ContributesAndroidInjector
    abstract fun provideContainerFragment(): UserProfileContainerFragment

    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): UserProfileFragment

    @ContributesAndroidInjector
    abstract fun providePostsFragment(): UserPostsFragment
}