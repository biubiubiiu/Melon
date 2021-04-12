package app.melon.event.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.entities.Event
import app.melon.event.data.EventRepository
import javax.inject.Inject

class UpdateEventDetail @Inject constructor(
    private val repo: EventRepository
) : SuspendingWorkInteractor<String, Event>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: String): Event {
        loadingState.addLoader()
        return repo.getEventDetail(params).also { loadingState.removeLoader() }
    }
}