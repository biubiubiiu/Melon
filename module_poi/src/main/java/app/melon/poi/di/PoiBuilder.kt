package app.melon.poi.di

import app.melon.base.scope.ActivityScope
import app.melon.poi.PoiDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class PoiBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectPoiDetailActivity(): PoiDetailActivity
}