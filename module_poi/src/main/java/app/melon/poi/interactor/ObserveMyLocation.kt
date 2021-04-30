package app.melon.poi.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.location.LocationHelper
import app.melon.location.SimplifiedLocation
import javax.inject.Inject


internal class ObserveMyLocation @Inject constructor(
    private val locationHelper: LocationHelper
) : SuspendingWorkInteractor<Unit, SimplifiedLocation?>() {

    override suspend fun doWork(params: Unit): SimplifiedLocation? {
        return locationHelper.getLocation()
    }
}