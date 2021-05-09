package app.melon.event.di

import app.melon.event.EventService
import app.melon.event.api.IEventService
import app.melon.event.data.EventApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(
    includes = [
        EventBuilder::class,
        EventServiceBinds::class,
        RemoteServiceModule::class
    ]
)
class EventModule {

    @Singleton
    @Provides
    fun provideEventService() = EventService()
}

@Module
internal abstract class EventServiceBinds {
    @Binds
    internal abstract fun bindEventService(service: EventService): IEventService
}

@Module
internal class RemoteServiceModule {
    @Provides
    internal fun provideEventApiService(retrofit: Retrofit) = retrofit.create(EventApi::class.java)
}