package app.melon.user.di

import app.melon.user.UserProfileActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserBuilder {
    @ContributesAndroidInjector
    abstract fun profileActivity(): UserProfileActivity
}