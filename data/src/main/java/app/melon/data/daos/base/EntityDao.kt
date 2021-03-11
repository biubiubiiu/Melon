package app.melon.data.daos.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import app.melon.data.entities.MelonEntity

/**
 * source: https://github.com/chrisbanes/tivi
 */
abstract class EntityDao<in E : MelonEntity> {

    @Insert
    abstract suspend fun insert(entity: E): Long

    @Insert
    abstract suspend fun insertAll(vararg entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: List<E>)

    @Update
    abstract suspend fun update(entity: E)

    @Delete
    abstract suspend fun deleteEntity(entity: E): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    suspend fun insertOrUpdate(entity: E): Long {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(entities: List<E>) {
        entities.forEach {
            insertOrUpdate(it)
        }
    }
}