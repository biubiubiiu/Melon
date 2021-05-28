package app.melon.poi.ui.overlay

import app.melon.location.toLatLng
import app.melon.poi.R
import app.melon.util.extensions.getResourceColor
import com.amap.api.maps2d.AMap
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.WalkPath
import com.amap.api.services.route.WalkStep


internal class WalkRouteOverlay constructor(
    override val mAMap: AMap,
    private val walkPath: WalkPath,
    start: LatLonPoint,
    end: LatLonPoint
) : RouteOverlay() {

    override val startPoint = start.toLatLng()
    override val endPoint = end.toLatLng()

    override val routeWidth = 12f
    override val routeColor = getResourceColor(R.color.route_overlay)

    override fun addToMap() {
        try {
            val walkPaths = walkPath.steps
            mPolylineOptions.add(startPoint)
            walkPaths.forEach { walkStep ->
                addWalkPolyLines(walkStep)
            }
            mPolylineOptions.add(endPoint)
            showPolyline()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun addWalkPolyLines(walkStep: WalkStep) {
        mPolylineOptions.addAll(walkStep.polyline.map { it.toLatLng() })
    }

    private fun showPolyline() {
        addPolyLine(mPolylineOptions)
    }
}