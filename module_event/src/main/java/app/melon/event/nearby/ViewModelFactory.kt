package app.melon.event.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.melon.event.data.EventRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class ViewModelFactory @Inject constructor(
    private val repo: EventRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(NearbyEventsViewModel::class.java) -> NearbyEventsViewModel(repo)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}