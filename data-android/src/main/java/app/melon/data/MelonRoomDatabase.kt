package app.melon.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.melon.data.entities.Feed
import app.melon.data.entities.FollowingFeedEntry
import app.melon.data.entities.RecommendFeedEntry

/**
 * source: https://github.com/chrisbanes/tivi
 */
@Database(
    entities = [
        Feed::class,
        RecommendFeedEntry::class,
        FollowingFeedEntry::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MelonRoomDatabase : RoomDatabase(), MelonDatabase