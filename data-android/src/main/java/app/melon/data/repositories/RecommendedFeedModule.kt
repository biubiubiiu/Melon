package app.melon.data.repositories

import app.melon.data.daos.FeedDao
import app.melon.data.daos.RecommendedDao
import app.melon.data.entities.Feed
import app.melon.data.entities.RecommendFeedEntry
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
typealias RecommendedFeedStore = Store<RecommendedFeedModule.Params, List<RecommendFeedEntry>>

@Module
object RecommendedFeedModule {

    /**
     * Key: [Params]
     * Input: List<Pair<[Feed], [RecommendFeedEntry]>>
     * Output: List<[RecommendFeedEntry]>
     */
    @Provides
    @Singleton
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun get(
        dataSource: RecommendedFeedDataSource,
        recommendedDao: RecommendedDao,
        feedDao: FeedDao
    ): RecommendedFeedStore = StoreBuilder.from(
        fetcher = Fetcher.of { params: Params ->
            dataSource(params.time, params.page, params.pageSize).getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { params: Params ->
                recommendedDao.entriesObservable(params.page).map { entries ->
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
            writer = { params: Params, response: List<Pair<Feed, RecommendFeedEntry>> ->
                recommendedDao.withTransaction {
                    val entries = response.map { (feed, entry) ->
                        entry.copy(feedId = feedDao.getIdOrSavePlaceholder(feed), page = params.page)
                    }
                    if (params.page == 0) {
                        // If we've requested page 0, remove any existing entries first
                        recommendedDao.deleteAll()
                        recommendedDao.insertAll(entries)
                    } else {
                        recommendedDao.updatePage(params.page, entries)
                    }
                }
            },
            delete = { params ->
                recommendedDao.deletePage(params.page)
            },
            deleteAll = recommendedDao::deleteAll
        )
    ).build()

    data class Params(
        val time: Long,
        val page: Int,
        val pageSize: Int
    )
}