package app.melon.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.melon.event.data.EventRepository
import app.melon.event.interactor.UpdateEventList
import app.melon.event.mine.MyEventsViewModel
import app.melon.event.nearby.NearbyEventsViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton


@Singleton
internal class ViewModelFactory @Inject constructor(
    private val repo: EventRepository,
    private val updateEventList: Provider<UpdateEventList>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(NearbyEventsViewModel::class.java) -> NearbyEventsViewModel(
                repo
            )
            isAssignableFrom(MyEventsViewModel::class.java) -> MyEventsViewModel(
                updateEventList.get(),
                updateEventList.get()
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}