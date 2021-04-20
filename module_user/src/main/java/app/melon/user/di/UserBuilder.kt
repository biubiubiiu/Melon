package app.melon.user.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.user.EditProfileActivity
import app.melon.user.FollowersActivity
import app.melon.user.FollowingActivity
import app.melon.user.ProfileImageActivity
import app.melon.user.ui.CommonUserFragment
import app.melon.user.ui.ProfileFragment
import app.melon.user.ui.UserProfileContainerFragment
import app.melon.user.ui.detail.UserProfileFragment
import app.melon.user.ui.mine.MyProfileTabFragment
import app.melon.user.ui.posts.UserPostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectUserListFragment(): CommonUserFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectProfileContainerFragment(): UserProfileContainerFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectProfileFragment(): ProfileFragment

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectEditProfileActivity(): EditProfileActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectProfileImageActivity(): ProfileImageActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): UserProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providePostsFragment(): UserPostsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectMyProfileTabFragment(): MyProfileTabFragment

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectFollowersActivity(): FollowersActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectFollowingActivity(): FollowingActivity
}