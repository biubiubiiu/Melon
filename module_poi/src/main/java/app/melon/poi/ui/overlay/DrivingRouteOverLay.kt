package app.melon.poi.ui.overlay

import app.melon.location.toLatLng
import app.melon.poi.R
import app.melon.util.extensions.getResourceColor
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.LatLngBounds
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DrivePath


internal class DrivingRouteOverLay constructor(
    override val mAMap: AMap,
    private val drivePath: DrivePath,
    start: LatLonPoint,
    end: LatLonPoint,
    private val throughPointList: List<LatLonPoint> = emptyList()
) : RouteOverlay() {

    override val startPoint = start.toLatLng()
    override val endPoint = end.toLatLng()

    override val routeWidth = 12f
    override val routeColor = getResourceColor(R.color.route_overlay)

    override fun addToMap() {
        try {
            if (routeWidth == 0f) {
                return
            }
            mPolylineOptions.add(startPoint)
            for (step in drivePath.steps) {
                val latLonPoints = step.polyline
                for (point in latLonPoints) {
                    mPolylineOptions.add(point.toLatLng())
                }
            }
            mPolylineOptions.add(endPoint)
            showPolyline()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun showPolyline() {
        addPolyLine(mPolylineOptions)
    }

    override fun getLatLngBounds(startPoint: LatLng, endPoint: LatLng): LatLngBounds {
        val b = LatLngBounds.builder()
        b.include(LatLng(startPoint.latitude, startPoint.longitude))
        b.include(LatLng(endPoint.latitude, endPoint.longitude))
        throughPointList.forEach {
            b.include(LatLng(it.latitude, it.longitude))
        }
        return b.build()
    }
}