package app.melon.home.di

import dagger.Module

@Module(
    includes = [
        HomeBuilder::class,
        HomeServiceBinds::class
    ]
)
class HomeModule