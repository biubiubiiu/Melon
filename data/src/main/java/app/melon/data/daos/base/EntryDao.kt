package app.melon.data.daos.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Update
import app.melon.data.entities.Entry

/**
 * This interface represents a DAO which contains entities which are part of a single collective list.
 */
abstract class EntryDao<in E : Entry> {

    @Insert
    abstract suspend fun insert(entity: E)

    @Insert
    abstract suspend fun insertAll(vararg entity: E)

    @Insert
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