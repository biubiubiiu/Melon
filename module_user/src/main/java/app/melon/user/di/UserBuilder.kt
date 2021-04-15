package app.melon.user.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.user.EditProfileActivity
import app.melon.user.ProfileActivity
import app.melon.user.UserProfileActivity
import app.melon.user.ui.UserProfileContainerFragment
import app.melon.user.ui.detail.UserProfileFragment
import app.melon.user.ui.mine.MyProfileTabFragment
import app.melon.user.ui.posts.UserPostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun provideActivity(): UserProfileActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectMyProfileActivity(): ProfileActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectEditProfileActivity(): EditProfileActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideContainerFragment(): UserProfileContainerFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): UserProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providePostsFragment(): UserPostsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectMyProfileTabFragment(): MyProfileTabFragment
}