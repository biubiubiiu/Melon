package app.melon.settings.di

import app.melon.settings.theme.ThemePreferences
import app.melon.settings.theme.ThemePreferencesImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class ThemePreferenceModule {

    @Singleton
    @Binds
    abstract fun bindThemePreference(impl: ThemePreferencesImpl): ThemePreferences
}