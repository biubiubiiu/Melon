package app.melon.data.daos.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

/**
 * source: https://github.com/chrisbanes/tivi
 */
abstract class EntityDao<E> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: List<E>)

    @Update
    abstract suspend fun update(entity: E)

    @Delete
    abstract suspend fun deleteEntity(entity: E): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    open suspend fun insertOrUpdate(entity: E) {}

    @Transaction
    open suspend fun insertOrUpdate(entities: List<E>) {
        entities.forEach {
            insertOrUpdate(it)
        }
    }
}