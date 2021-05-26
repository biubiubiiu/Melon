package app.melon.user.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.user.EditProfileActivity
import app.melon.user.FollowersActivity
import app.melon.user.FollowingActivity
import app.melon.user.ProfileImageActivity
import app.melon.user.nearby.NearbyUserListFragment
import app.melon.user.ui.CommonUserFragment
import app.melon.user.ui.ProfileFragment
import app.melon.user.ui.UserProfileContainerFragment
import app.melon.user.ui.detail.UserProfileFragment
import app.melon.user.ui.mine.MyProfileTabFragment
import app.melon.user.ui.posts.UserPostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class UserBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectUserListFragment(): CommonUserFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectNearbyUserFragment(): NearbyUserListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectProfileContainerFragment(): UserProfileContainerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectProfileFragment(): ProfileFragment

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectEditProfileActivity(): EditProfileActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectProfileImageActivity(): ProfileImageActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideProfileFragment(): UserProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun providePostsFragment(): UserPostsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectMyProfileTabFragment(): MyProfileTabFragment

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectFollowersActivity(): FollowersActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectFollowingActivity(): FollowingActivity
}