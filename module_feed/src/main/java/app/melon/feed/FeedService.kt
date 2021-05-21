package app.melon.feed

import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FeedService @Inject constructor(
    private val repo: FeedRepository
) : IFeedService {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun collect(id: String) {
        ioScope.launch {
            repo.addFeedToCollection(id)
        }
    }
}