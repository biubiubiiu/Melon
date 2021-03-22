package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.Feed

@Dao
abstract class FeedDao : EntityDao<Feed>() {

    @Query("SELECT * FROM feeds WHERE feed_id = :id")
    abstract suspend fun getFeedWithId(id: String): Feed?

    suspend fun getFeedWithIdOrThrow(id: String): Feed {
        return getFeedWithId(id) ?: throw IllegalArgumentException("No feed with id $id in database")
    }

    @Query("DELETE FROM feeds WHERE feed_id = :id")
    abstract suspend fun delete(id: String)

    @Query("DELETE FROM feeds")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM feeds WHERE feed_id = :id")
    abstract suspend fun getFeedWithFeedId(id: String): Feed?

    @Query("DELETE FROM feeds WHERE feed_type = :type")
    abstract suspend fun deleteFeedByType(type: String)
}