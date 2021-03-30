package app.melon.group.data

import app.melon.data.entities.InterestGroup
import app.melon.group.service.GroupApiService
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val service: GroupApiService
) {

    suspend fun getFirstPageGroups(): List<InterestGroup> {
        return (0L..10L).map {
            InterestGroup(it, "", "", 0, "")
        }
    }
}