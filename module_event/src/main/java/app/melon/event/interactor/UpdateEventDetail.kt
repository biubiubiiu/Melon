package app.melon.event.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.data.EventRepository
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import javax.inject.Inject


class UpdateEventDetail @Inject constructor(
    private val repo: EventRepository
) : SuspendingWorkInteractor<String, Result<EventAndOrganiser>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: String): Result<EventAndOrganiser> {
        return try {
            loadingState.addLoader()
            repo.getEventDetail(params).also { loadingState.removeLoader() }
        } catch (e: Exception) {
            ErrorResult<EventAndOrganiser>(e).also { loadingState.removeLoader() }
        }
    }
}