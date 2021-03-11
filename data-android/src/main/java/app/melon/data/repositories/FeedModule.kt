package app.melon.data.repositories

import app.melon.data.daos.FeedDao
import app.melon.data.entities.Feed
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/**
 * source: https://github.com/chrisbanes/tivi
 */
typealias FeedStore = Store<Long, Feed>

@Module
object FeedModule {

    /**
     * Key: Long
     * Input: Feed
     * Output: Feed
     */
    @Provides
    @Singleton
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun provideFeedStore(
        feedDao: FeedDao,
        dataSource: FeedDataSource
    ): FeedStore = StoreBuilder.from(
        fetcher = Fetcher.of { id: Long ->
            dataSource.getFeed(feedDao.getFeedWithIdOrThrow(id)).getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { id: Long ->
                feedDao.getFeedWithIdFlow(id)
            },
            writer = { _, response ->
                feedDao.withTransaction {
                    feedDao.insertOrUpdate(response)
                }
            },
            delete = feedDao::delete,
            deleteAll = feedDao::deleteAll
        )
    ).build()
}