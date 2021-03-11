package app.melon.data.repositories

import app.melon.data.daos.FeedDao
import app.melon.data.daos.FollowingDao
import app.melon.data.entities.Feed
import app.melon.data.entities.FollowingFeedEntry
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


/**
 * source: https://github.com/chrisbanes/tivi
 */
typealias FollowingFeedStore = Store<FollowingFeedModule.Params, List<FollowingFeedEntry>>

@Module
object FollowingFeedModule {

    /**
     * Key: [Params]
     * Input: List<Pair<[Feed], [FollowingFeedEntry]>>
     * Output: List<[FollowingFeedEntry]>
     */
    @Provides
    @Singleton
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun get(
        dataSource: FollowingFeedDataSource,
        followingDao: FollowingDao,
        feedDao: FeedDao
    ): FollowingFeedStore = StoreBuilder.from(
        fetcher = Fetcher.of { params: Params ->
            dataSource(params.time, params.page, params.pageSize).getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { params: Params ->
                followingDao.entriesObservable(params.page).map { entries ->
                    when {
                        // Store only treats null as 'no value', so convert to null
                        entries.isEmpty() -> null
                        // If the request is expired, our data is stale
//                        lastRequestStore.isRequestExpired(Duration.ofHours(3)) -> null // TODO consider this
                        // Otherwise, our data is fresh and valid
                        else -> entries
                    }
                }
            },
            writer = { params: Params, response: List<Pair<Feed, FollowingFeedEntry>> ->
                followingDao.withTransaction {
                    val entries = response.map { (feed, entry) ->
                        entry.copy(feedId = feedDao.getIdOrSavePlaceholder(feed), page = params.page)
                    }
                    if (params.page == 0) {
                        // If we've requested page 0, remove any existing entries first
                        followingDao.deleteAll()
                        followingDao.insertAll(entries)
                    } else {
                        followingDao.updatePage(params.page, entries)
                    }
                }
            },
            delete = { params ->
                followingDao.deletePage(params.page)
            },
            deleteAll = followingDao::deleteAll
        )
    ).build()

    data class Params(
        val time: Long,
        val page: Int,
        val pageSize: Int
    )
}