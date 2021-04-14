package app.melon.data.resultentities

import app.melon.data.entities.Entry
import app.melon.data.entities.MelonEntity
import java.util.Objects


abstract class EntryWithEntity<ENTRY : Entry, ENTITY : MelonEntity> {

    abstract val entry: ENTRY
    abstract val relations: List<ENTITY>

    val relatedItem: ENTITY
        get() {
            require(relations.size == 1)
            return relations[0]
        }

    fun generateStableId(): Long {
        return Objects.hash(entry::class.java.name, entry.relatedId).toLong()
    }
}