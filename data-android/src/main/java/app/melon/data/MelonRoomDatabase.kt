package app.melon.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import app.melon.data.entities.Feed
import app.melon.data.entities.FollowingFeedEntry
import app.melon.data.entities.RecommendedFeedEntry

/**
 * source: https://github.com/chrisbanes/tivi
 */
@Database(
    entities = [
        Feed::class,
        RecommendedFeedEntry::class,
        FollowingFeedEntry::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MelonRoomDatabase : RoomDatabase(), MelonDatabase {
    override suspend fun <R> runWithTransaction(block: suspend () -> R): R = withTransaction(block)
}