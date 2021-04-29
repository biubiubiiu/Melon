package app.melon.poi.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.location.RouteShareHelper
import app.melon.location.SimplifiedLocation
import app.melon.util.base.Result
import javax.inject.Inject


internal class ShareRoute @Inject constructor(
    private val routeShareHelper: RouteShareHelper
) : SuspendingWorkInteractor<ShareRoute.Params, Result<String?>>() {

    internal val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<String?> {
        loadingState.addLoader()
        val result = routeShareHelper.shareRoute(params.start, params.dest)
        return result.also { loadingState.removeLoader() }
    }

    internal data class Params(
        val start: SimplifiedLocation,
        val dest: SimplifiedLocation
    )
}