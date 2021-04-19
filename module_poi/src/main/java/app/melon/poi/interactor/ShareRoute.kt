package app.melon.poi.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.location.RouteShareHelper
import app.melon.location.SimplifiedLocation
import app.melon.util.base.Result
import javax.inject.Inject


class ShareRoute @Inject constructor(
    private val routeShareHelper: RouteShareHelper
) : SuspendingWorkInteractor<ShareRoute.Params, Result<String?>>() {

    override suspend fun doWork(params: Params): Result<String?> {
        return routeShareHelper.shareRoute(params.start, params.dest)
    }

    data class Params(
        val start: SimplifiedLocation,
        val dest: SimplifiedLocation
    )
}