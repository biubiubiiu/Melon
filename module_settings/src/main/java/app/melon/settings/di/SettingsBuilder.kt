package app.melon.settings.di

import app.melon.base.scope.FragmentScope
import app.melon.settings.SettingsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectSettingsListFragment(): SettingsListFragment
}