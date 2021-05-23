package app.melon.im.di

import app.melon.im.IIMService
import app.melon.im.IMService
import dagger.Binds
import dagger.Module


@Module
abstract class IMServiceBinds {

    @Binds
    abstract fun bindIMService(service: IMService): IIMService
}