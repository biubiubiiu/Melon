package app.melon.poi.ui.overlay

import android.graphics.Color
import app.melon.location.toLatLng
import app.melon.poi.R
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.WalkPath
import com.amap.api.services.route.WalkStep


internal class WalkRouteOverlay constructor(
    override val mAMap: AMap,
    private val walkPath: WalkPath,
    start: LatLonPoint,
    end: LatLonPoint
) : RouteOverlay() {

    private val walkStationDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.amap_man)

    override val startPoint = start.toLatLng()
    override val endPoint = end.toLatLng()

    override val routeWidth = 12f
    override val routeColor = Color.parseColor("#6db74d")

    override fun addToMap() {
        try {
            val walkPaths = walkPath.steps
            mPolylineOptions.add(startPoint)
            walkPaths.forEach { walkStep ->
                addWalkStationMarkers(walkStep, walkStep.polyline[0].toLatLng())
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

    private fun addWalkStationMarkers(walkStep: WalkStep, position: LatLng) {
        addStationMarker(MarkerOptions()
            .position(position)
            .title("""
                方向:${walkStep.action}
                道路:${walkStep.road}
                """.trimIndent()
            )
            .snippet(walkStep.instruction)
            .visible(nodeIconVisible)
            .anchor(0.5f, 0.5f)
            .icon(walkStationDescriptor))
    }

    private fun showPolyline() {
        addPolyLine(mPolylineOptions)
    }
}