package app.melon.group.data

import app.melon.data.MelonDatabase
import app.melon.data.entities.Group
import app.melon.group.service.GroupApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GroupRepository @Inject constructor(
    private val service: GroupApiService,
    private val database: MelonDatabase
) {

    suspend fun getFirstPageGroups(): List<Group> {
        return (0L..10L).map {
            Group(it.toString(), "", "", "", 2)
        }
    }
}