package app.melon.feed.data

import app.melon.data.resultentities.FeedAndAuthor

sealed class FeedStatus<out T : Any> {

    class Success(val data: FeedAndAuthor) : FeedStatus<FeedAndAuthor>()

    object Deleted : FeedStatus<Unit>()

    sealed class Error(val throwable: Throwable) : FeedStatus<Nothing>() {
        class Generic(throwable: Throwable) : Error(throwable)
    }
}
