package app.melon.poi.interactor

import app.melon.location.RouteSearchHelper
import app.melon.location.SimplifiedLocation
import javax.inject.Inject


/**
 * Just a wrapper :)
 */
internal class UpdateWalkRoute @Inject constructor(
    private val routeSearchHelper: RouteSearchHelper
) {

    suspend fun search(start: SimplifiedLocation, destination: SimplifiedLocation) =
        routeSearchHelper.searchWalkRoute(start, destination)

}