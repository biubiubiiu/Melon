package app.melon.event

import app.melon.base.network.MelonApiService
import app.melon.data.constants.EventPageType
import app.melon.event.data.EventApi
import app.melon.event.data.remote.EventStruct
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EventApiService @Inject constructor(
    private val api: EventApi
) : MelonApiService() {

    suspend fun events(
        longitude: Float,
        latitude: Float,
        page: Int,
        pageSize: Int
    ): Result<List<EventStruct>> {
        return call {
            api.events(longitude, latitude, page, pageSize)
        }
    }

    suspend fun events(
        @EventPageType type: Int,
        page: Int,
        pageSize: Int
    ): Result<List<EventStruct>> {
        return call {
            api.events(type, page, pageSize)
        }
    }

    suspend fun detail(
        id: String
    ): Result<EventStruct> {
        return call {
            api.detail(id)
        }
    }
}