package app.melon.data.daos.base

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import app.melon.data.entities.PaginatedEntry


/**
 * source: https://github.com/chrisbanes/tivi
 */
abstract class PaginatedEntryDao<EC : PaginatedEntry> : EntryDao<EC>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: EC)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(vararg entity: EC)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(entities: List<EC>)

    abstract suspend fun deleteAll()

    abstract suspend fun deletePage(page: Int)

    abstract suspend fun getLastPage(): Int?
}

abstract class TypedPaginatedEntryDao<EC : PaginatedEntry, T : Any> : EntryDao<EC>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: EC)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(vararg entity: EC)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(entities: List<EC>)

    open suspend fun deleteEntryByType(type: T) {}

    open suspend fun deletePage(page: Int, type: T) {}

    open suspend fun getLastPage(type: T): Int? = null
}
