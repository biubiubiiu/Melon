package app.melon.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import app.melon.data.entities.AttendEvent
import app.melon.data.entities.JoinGroup
import app.melon.data.entities.Event
import app.melon.data.entities.EventEntry
import app.melon.data.entities.Feed
import app.melon.data.entities.FeedEntry
import app.melon.data.entities.Group
import app.melon.data.entities.GroupEntry
import app.melon.data.entities.User


@Database(
    entities = [
        Feed::class,
        FeedEntry::class,
        User::class,
        Event::class,
        EventEntry::class,
        AttendEvent::class,
        Group::class,
        GroupEntry::class,
        JoinGroup::class
    ],
    version = 14,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MelonRoomDatabase : RoomDatabase(), MelonDatabase {
    override suspend fun <R> runWithTransaction(block: suspend () -> R): R = withTransaction(block)
}