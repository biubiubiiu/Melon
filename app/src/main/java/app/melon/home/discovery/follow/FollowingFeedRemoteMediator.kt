//package app.melon.home.discovery.follow
//
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import androidx.room.withTransaction
//import app.melon.data.MelonRoomDatabase
//import app.melon.data.entities.FollowingFeed
//import app.melon.data.entities.RemoteKeys
//import app.melon.data.resultentities.FollowingEntryWithFeed
//import app.melon.data.services.FeedApiService
//import coil.network.HttpException
//import java.io.IOException
//import java.io.InvalidObjectException
//
///**
// * Taken from Android Open Source
// */
//@OptIn(ExperimentalPagingApi::class)
//class FollowingFeedRemoteMediator(
//    private val timestamp: Long,
//    private val service: FeedApiService,
//    private val database: MelonRoomDatabase
//) : RemoteMediator<Int, FollowingEntryWithFeed>() {
//
//    private companion object {
//        const val STARTING_PAGE_INDEX = 0
//    }
//
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, FollowingEntryWithFeed>): MediatorResult {
//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
//
//                // If the previous key is null, then we can't request more data
//                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                remoteKeys?.nextKey ?: throw InvalidObjectException("Remote key should not be null for $loadType")
//            }
//        }
//
//        try {
//            val apiResponse = service.fetchFollowingList(timestamp, page, state.config.pageSize)
//
//            val items = apiResponse.data?.items ?: emptyList()
//            val endOfPaginationReached = items.isEmpty()
//            database.withTransaction {
//                // clear all tables in the database
//                if (loadType == LoadType.REFRESH) {
//                    database.followingDao().clearAll()
//                }
//                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                val keys = items.map {
//                    RemoteKeys(
//                        feedId = it.id,
//                        prevKey = prevKey,
//                        nextKey = nextKey
//                    )
//                }
//                database.followingDao().insertAll(items)
//            }
//            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//        } catch (exception: IOException) {
//            return MediatorResult.Error(exception)
//        } catch (exception: HttpException) {
//            return MediatorResult.Error(exception)
//        }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FollowingFeed>): RemoteKeys? {
//        // Get the last page that was retrieved, that contained items.
//        // From that last page, get the last item
//        return state.lastItemOrNull()?.let { item ->
//            // Get the remote keys of the last item retrieved
//            database.remoteKeysDao().remoteKeysFeedId(item.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FollowingFeed>): RemoteKeys? {
//        // Get the first page that was retrieved, that contained items.
//        // From that first page, get the first item
//        return state.firstItemOrNull()?.let { item ->
//            // Get the remote keys of the first items retrieved
//            database.remoteKeysDao().remoteKeysFeedId(item.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(
//        state: PagingState<Int, FollowingFeed>
//    ): RemoteKeys? {
//        // The paging library is trying to load data after the anchor position
//        // Get the item closest to the anchor position
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { itemId ->
//                database.remoteKeysDao().remoteKeysFeedId(itemId)
//            }
//        }
//    }
//}