package app.melon.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import app.melon.data.entities.ANExploreFeedEntry
import app.melon.data.entities.ANSchoolFeedEntry
import app.melon.data.entities.ANTrendingFeedEntry
import app.melon.data.entities.Feed
import app.melon.data.entities.FollowingFeedEntry
import app.melon.data.entities.JoiningEvent
import app.melon.data.entities.OrganisedEvent
import app.melon.data.entities.RecommendedFeedEntry

/**
 * source: https://github.com/chrisbanes/tivi
 */
@Database(
    entities = [
        Feed::class,
        RecommendedFeedEntry::class,
        FollowingFeedEntry::class,
        ANExploreFeedEntry::class,
        ANSchoolFeedEntry::class,
        ANTrendingFeedEntry::class,
        JoiningEvent::class,
        OrganisedEvent::class
    ],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MelonRoomDatabase : RoomDatabase(), MelonDatabase {
    override suspend fun <R> runWithTransaction(block: suspend () -> R): R = withTransaction(block)
}