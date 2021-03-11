package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.Feed
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FeedDao : EntityDao<Feed>() {

    @Query("SELECT * FROM feeds WHERE id = :id")
    abstract fun getFeedWithIdFlow(id: Long): Flow<Feed>

    @Query("SELECT * FROM feeds WHERE id = :id")
    abstract suspend fun getFeedWithId(id: Long): Feed?

    suspend fun getFeedWithIdOrThrow(id: Long): Feed {
        return getFeedWithId(id) ?: throw IllegalArgumentException("No feed with id $id in database")
    }

    @Query("DELETE FROM feeds WHERE id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM feeds")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM feeds WHERE feed_id = :id")
    abstract suspend fun getFeedWithFeedId(id: String): Feed?

    suspend fun getIdOrSavePlaceholder(item: Feed): Long {
        val localItem = getFeedWithFeedId(id = item.feedId)
        return if (localItem != null) {
            update(item) // TODO [item] here may only contains a partition of information, so we need to merge local item and remote item
            localItem.id
        } else {
            insert(item)
        }
    }
}