package app.melon.data

import app.melon.data.daos.FeedDao
import app.melon.data.daos.FollowingDao
import app.melon.data.daos.RecommendedDao

interface MelonDatabase {
    fun feedDao(): FeedDao
    fun recommendedDao(): RecommendedDao
    fun followingDao(): FollowingDao

    suspend fun <R> runWithTransaction(block: suspend () -> R): R
}