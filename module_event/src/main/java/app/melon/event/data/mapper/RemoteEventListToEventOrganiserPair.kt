package app.melon.event.data.mapper

import app.melon.data.entities.Event
import app.melon.data.entities.User
import app.melon.event.data.remote.EventStruct
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteEventListToEventOrganiserPair @Inject constructor() : Mapper<EventStruct, Pair<Event, User>> {
    override suspend fun map(from: EventStruct): Pair<Event, User> {
        val event = Event(
            id = from.id,
            organiserUid = from.organiser?.id,
            title = from.title,
            content = from.content,
            startTime = from.startTime,
            endTime = from.endTime,
            location = from.location,
            cost = from.cost,
            demand = from.demand
        )
        val user = User(
            id = from.organiser?.id ?: "",
            username = from.organiser?.username,
            avatarUrl = from.organiser?.avatarUrl
        )
        return event to user
    }
}