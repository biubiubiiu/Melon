package app.melon.im.di

import dagger.Module


@Module(
    includes = [
        IMInitModule::class
    ]
)
class IMModule