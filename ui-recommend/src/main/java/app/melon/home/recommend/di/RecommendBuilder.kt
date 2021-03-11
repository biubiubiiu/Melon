package app.melon.home.recommend.di

import app.melon.home.recommend.ui.RecommendFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RecommendBuilder {
    @ContributesAndroidInjector
    abstract fun recommendFragment(): RecommendFragment
}