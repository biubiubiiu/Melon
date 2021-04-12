package app.melon.event.detail

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.entities.Event
import app.melon.event.data.EventRepository
import javax.inject.Inject

class ChangeEventStatus @Inject constructor(
    private val repo: EventRepository
) : SuspendingWorkInteractor<ChangeEventStatus.Params, Event>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Event {
        loadingState.addLoader()
        return when (params.action) {
            Action.TOGGLE -> {
                if (repo.isJoiningEvent(params.event.id)) {
                    leave(params.event)
                } else {
                    join(params.event)
                }
            }
            Action.JOIN -> join(params.event)
            Action.LEAVE -> leave(params.event)
        }.also { loadingState.removeLoader() }
    }

    private suspend fun join(event: Event) = repo.addJoiningEvent(event)

    private suspend fun leave(event: Event) = repo.removeJoiningEvent(event)

    data class Params(
        val event: Event,
        val action: Action
    )

    enum class Action { JOIN, LEAVE, TOGGLE }

}