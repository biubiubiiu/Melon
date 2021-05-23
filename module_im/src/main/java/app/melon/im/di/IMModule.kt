package app.melon.im.di

import dagger.Module


@Module(
    includes = [
        IMInitModule::class,
        IMServiceBinds::class
    ]
)
class IMModule