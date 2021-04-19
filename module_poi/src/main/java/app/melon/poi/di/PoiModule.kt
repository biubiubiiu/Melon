package app.melon.poi.di

import dagger.Module


@Module(
    includes = [
        PoiBuilder::class
    ]
)
class PoiModule