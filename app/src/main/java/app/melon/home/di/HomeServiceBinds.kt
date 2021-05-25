package app.melon.home.di

import app.melon.home.HomeService
import app.melon.home.api.IHomeService
import dagger.Binds
import dagger.Module

@Module
abstract class HomeServiceBinds {

    @Binds
    abstract fun homeServiceBinds(service: HomeService): IHomeService
}