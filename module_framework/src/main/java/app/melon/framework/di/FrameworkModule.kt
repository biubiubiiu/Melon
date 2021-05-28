package app.melon.framework.di

import dagger.Module


@Module(
    includes = [
        LocaleModule::class
    ]
)
class FrameworkModule