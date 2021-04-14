package app.melon.event.detail

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.entities.Event
import app.melon.event.data.EventRepository
import javax.inject.Inject

class ChangeEventStatus @Inject constructor(
    private val repo: EventRepository
    // TODO inject UserManager
) : SuspendingWorkInteractor<ChangeEventStatus.Params, Event>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Event {
        loadingState.addLoader()
        return when (params.action) {
            Action.TOGGLE -> {
                if (repo.isJoiningEvent(params.eventId)) {
                    leave(params.eventId)
                } else {
                    join(params.eventId)
                }
            }
            Action.JOIN -> join(params.eventId)
            Action.LEAVE -> leave(params.eventId)
        }.also { loadingState.removeLoader() }
    }

    private suspend fun join(eventId: String) = repo.addJoiningEvent(eventId)

    private suspend fun leave(eventId: String) = repo.removeJoiningEvent(eventId)

    data class Params(
        val eventId: String,
        val action: Action
    )

    enum class Action { JOIN, LEAVE, TOGGLE }
}