package app.melon.event.di

import app.melon.event.EventService
import app.melon.event.api.IEventService
import app.melon.event.data.EventApiService
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
abstract class EventServiceBinds {
    @Binds
    abstract fun bindEventService(service: EventService): IEventService
}

@Module
internal class RemoteServiceModule {
    @Provides
    fun provideEventApiService(retrofit: Retrofit) = retrofit.create(EventApiService::class.java)
}