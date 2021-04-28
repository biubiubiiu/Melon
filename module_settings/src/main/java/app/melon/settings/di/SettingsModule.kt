package app.melon.settings.di

import dagger.Module

@Module(
    includes = [
        ThemePreferenceModule::class,
        SettingsBuilder::class
    ]
)
class SettingsModule