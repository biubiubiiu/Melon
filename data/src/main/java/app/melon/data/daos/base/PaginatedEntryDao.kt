package app.melon.data.daos.base

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import app.melon.data.PaginatedEntry
import app.melon.data.resultentities.EntryWithFeed

/**
 * source: https://github.com/chrisbanes/tivi
 */
abstract class PaginatedEntryDao<EC : PaginatedEntry, LI : EntryWithFeed<EC>> : EntryDao<EC, LI>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: EC): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(vararg entity: EC)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertAll(entities: List<EC>)

    abstract suspend fun deletePage(page: Int)
    abstract suspend fun getLastPage(): Int?
}
