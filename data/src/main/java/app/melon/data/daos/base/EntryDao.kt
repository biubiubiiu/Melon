package app.melon.data.daos.base

import app.melon.data.Entry
import app.melon.data.resultentities.EntryWithFeed

/**
 * This interface represents a DAO which contains entities which are part of a single collective list.
 *
 * source: https://github.com/chrisbanes/tivi
 */
abstract class EntryDao<EC : Entry, LI : EntryWithFeed<EC>> : EntityDao<EC>() {
    abstract suspend fun deleteAll()
}