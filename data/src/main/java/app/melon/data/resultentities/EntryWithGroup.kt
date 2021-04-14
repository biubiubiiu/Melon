package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Group
import app.melon.data.entities.GroupEntry


data class EntryWithGroup(

    @Embedded val entry: GroupEntry,
    @Relation(
        parentColumn = "group_id",
        entityColumn = "group_id"
    )
    val relations: List<Group>

) {
    val relatedItem: Group
        get() {
            require(relations.size == 1)
            return relations[0]
        }
}