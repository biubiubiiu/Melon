package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.constants.GroupPageType
import app.melon.data.daos.base.TypedPaginatedEntryDao
import app.melon.data.entities.GroupEntry
import app.melon.data.resultentities.EntryWithGroup


@Dao
abstract class GroupEntryDao : TypedPaginatedEntryDao<GroupEntry, Int>() {

    @Transaction
    @Query("SELECT * FROM group_entries WHERE page_type = :type ORDER BY page ASC, page_order ASC")
    abstract fun groupDataSource(@GroupPageType type: Int): PagingSource<Int, EntryWithGroup>

    @Query("DELETE FROM group_entries WHERE page = :page AND page_type = :type")
    abstract override suspend fun deletePage(page: Int, @GroupPageType type: Int)

    @Query("DELETE FROM group_entries WHERE page_type = :type")
    abstract override suspend fun deleteEntryByType(@GroupPageType type: Int)

    @Query("SELECT MAX(page) FROM group_entries WHERE page_type = :type")
    abstract override suspend fun getLastPage(@GroupPageType type: Int): Int?
}