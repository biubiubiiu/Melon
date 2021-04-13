package app.melon.data

import app.melon.data.daos.ANExploreDao
import app.melon.data.daos.ANSchoolDao
import app.melon.data.daos.ANTrendingDao
import app.melon.data.daos.EventDao
import app.melon.data.daos.FeedDao
import app.melon.data.daos.FollowingDao
import app.melon.data.daos.OrganisedEventDao
import app.melon.data.daos.RecommendedDao

interface MelonDatabase {
    fun feedDao(): FeedDao
    fun recommendedDao(): RecommendedDao
    fun followingDao(): FollowingDao
    fun ANSchoolDao(): ANSchoolDao
    fun ANExploreDao(): ANExploreDao
    fun ANTrendingDao(): ANTrendingDao
    fun eventDao(): EventDao
    fun organisedEventDao(): OrganisedEventDao

    suspend fun <R> runWithTransaction(block: suspend () -> R): R
}