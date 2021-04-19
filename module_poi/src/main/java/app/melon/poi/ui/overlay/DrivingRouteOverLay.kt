package app.melon.poi.ui.overlay

import android.graphics.Color
import app.melon.location.toLatLng
import app.melon.poi.R
import app.melon.util.extensions.getResourceColor
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.LatLngBounds
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.PolylineOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.DriveStep
import com.amap.api.services.route.TMC
import java.util.*

class DrivingRouteOverLay constructor(
    override val mAMap: AMap,
    private val drivePath: DrivePath,
    start: LatLonPoint,
    end: LatLonPoint,
    private val throughPointList: List<LatLonPoint> = emptyList()
) : RouteOverlay() {

    private val throughPointMarkerList: MutableList<Marker> = ArrayList()
    private var throughPointMarkerVisible = true
    private val tmcs = arrayListOf<TMC>()

    var isColorFullLine = true

    override val startPoint = start.toLatLng()
    override val endPoint = end.toLatLng()

    override val routeWidth = 12f
    override val routeColor = getResourceColor(R.color.route_overlay)

    private val throughPointBitDes = BitmapDescriptorFactory.fromResource(R.drawable.amap_through)

    private val mLatLngsOfPath: MutableList<LatLng> = ArrayList()

    private val driveBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.amap_car)

    override fun removeFromMap() {
        try {
            super.removeFromMap()
            throughPointMarkerList.forEach { it.remove() }
            throughPointMarkerList.clear()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 添加驾车路线添加到地图上显示。
     */
    override fun addToMap() {
        try {
            if (routeWidth == 0f) {
                return
            }
            mPolylineOptions.add(startPoint)
            for (step in drivePath.steps) {
                tmcs.addAll(step.tmCs)
                val latLonPoints = step.polyline
                addDrivingStationMarkers(step, latLonPoints[0].toLatLng())
                for (point in latLonPoints) {
                    mPolylineOptions.add(point.toLatLng())
                    mLatLngsOfPath.add(point.toLatLng())
                }
            }
            mPolylineOptions.add(endPoint)
            addThroughPointMarker()
            if (isColorFullLine && tmcs.size > 0) {
                colorWayUpdate(tmcs)
            } else {
                showPolyline()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun showPolyline() {
        addPolyLine(mPolylineOptions)
    }

    private fun colorWayUpdate(tmcSection: List<TMC>) {
        if (tmcSection.isEmpty()) {
            return
        }
        var polylineOption = PolylineOptions().apply {
            width(routeWidth)
            color(routeColor)
            add(startPoint)
            add(tmcSection[0].polyline[0].toLatLng())
        }
        for (status in tmcSection) {
            val polyline = status.polyline
            polylineOption.color(getcolor(status.status))
            var lastLanLng: LatLng? = null
            for (j in 1 until polyline.size) {
                lastLanLng = polyline[j].toLatLng()
                polylineOption.add(lastLanLng)
            }
            mAMap.addPolyline(polylineOption)

            polylineOption = PolylineOptions().apply {
                width(routeWidth)
                color(routeColor)
            }
            if (lastLanLng != null) {
                polylineOption.add(lastLanLng)
            }
        }
        polylineOption.add(endPoint)
        mAMap.addPolyline(polylineOption)
    }

    private fun getcolor(status: String): Int {
        return when (status) {
            "畅通" -> Color.GREEN
            "缓行" -> Color.YELLOW
            "拥堵" -> Color.RED
            "严重拥堵" -> Color.parseColor("#990033")
            else -> Color.parseColor("#537edc")
        }
    }

    private fun addDrivingStationMarkers(driveStep: DriveStep, latLng: LatLng) {
        addStationMarker(MarkerOptions()
            .position(latLng)
            .title("""
                方向:${driveStep.action}
                道路:${driveStep.road}
                """.trimIndent()
            )
            .snippet(driveStep.instruction).visible(nodeIconVisible)
            .anchor(0.5f, 0.5f).icon(driveBitmapDescriptor))
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

    fun setThroughPointIconVisibility(visible: Boolean) {
        try {
            throughPointMarkerVisible = visible
            throughPointMarkerList.forEach { it.isVisible = visible }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun addThroughPointMarker() {
        throughPointList.forEach {
            throughPointMarkerList.add(
                mAMap.addMarker(MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .visible(throughPointMarkerVisible)
                    .icon(throughPointBitDes)
                    .title("\u9014\u7ECF\u70B9")
                )
            )
        }
    }
}