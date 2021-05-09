package app.melon.event.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.data.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateEventDetail @Inject constructor(
    private val repo: EventRepository
) : SuspendingWorkInteractor<String, Result<EventAndOrganiser>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: String): Result<EventAndOrganiser> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repo.getEventDetail(params)
        }.also { loadingState.removeLoader() }
    }
}