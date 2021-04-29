package app.melon.poi.di

import app.melon.poi.PoiService
import app.melon.poi.api.IPoiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(
    includes = [
        PoiBuilder::class,
        PoiServiceBinds::class
    ]
)
class PoiModule {

    @Singleton
    @Provides
    internal fun providePoiService() = PoiService()
}

@Module
internal abstract class PoiServiceBinds {
    @Binds
    abstract fun bindPoiService(service: PoiService): IPoiService
}