package app.melon.poi.di

import app.melon.base.scope.ActivityScope
import app.melon.poi.PoiDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class PoiBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun injectPoiDetailActivity(): PoiDetailActivity
}