package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.Feed
import app.melon.data.resultentities.FeedAndAuthor


@Dao
abstract class FeedDao : EntityDao<Feed>() {

    @Query("SELECT * FROM feeds WHERE feed_id = :id")
    abstract suspend fun getFeedWithId(id: String): Feed?

    suspend fun getFeedWithIdOrThrow(id: String): Feed {
        return getFeedWithId(id) ?: throw IllegalArgumentException("No feed with id $id in database")
    }

    @Transaction
    @Query("SELECT * FROM feeds WHERE feed_id = :id")
    abstract suspend fun getFeedAndAuthorWithId(id: String): FeedAndAuthor?

    @Transaction
    @Query("SELECT * FROM feeds WHERE feed_id IN (:ids)")
    abstract suspend fun getFeedAndAuthorWithId(ids: List<String>): List<FeedAndAuthor>

    @Query("DELETE FROM feeds WHERE feed_id = :id")
    abstract suspend fun delete(id: String)

    @Transaction
    open suspend fun deleteAll(ids: List<String>) {
        ids.forEach {
            delete(it)
        }
    }

    @Query("DELETE FROM feeds")
    abstract suspend fun deleteAll()

    override suspend fun insertOrUpdate(entity: Feed) {
        val item = getFeedWithId(entity.id)
        if (item == null) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}