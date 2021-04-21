package app.melon.splash.di

import app.melon.base.scope.ActivityScope
import app.melon.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SplashBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectSplashActivity(): SplashActivity
}