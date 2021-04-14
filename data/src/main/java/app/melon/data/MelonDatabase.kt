package app.melon.data

import app.melon.data.daos.AttendEventDao
import app.melon.data.daos.EventDao
import app.melon.data.daos.EventEntryDao
import app.melon.data.daos.FeedDao
import app.melon.data.daos.FeedEntryDao
import app.melon.data.daos.GroupDao
import app.melon.data.daos.GroupEntryDao
import app.melon.data.daos.JoinGroupDao
import app.melon.data.daos.UserDao


interface MelonDatabase {
    fun feedDao(): FeedDao
    fun feedEntryDao(): FeedEntryDao
    fun userDao(): UserDao
    fun eventDao(): EventDao
    fun eventEntryDao(): EventEntryDao
    fun attendEventDao(): AttendEventDao
    fun groupDao(): GroupDao
    fun groupEntryDao(): GroupEntryDao
    fun joinGroupDao(): JoinGroupDao

    suspend fun <R> runWithTransaction(block: suspend () -> R): R
}