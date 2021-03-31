package app.melon.feed.anonymous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.melon.feed.data.FeedRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class ViewModelFactory @Inject constructor(
    private val feedRepository: FeedRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(AnonymousFeedViewModel::class.java) -> AnonymousFeedViewModel(feedRepository)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}