package app.melon.poi.ui.overlay

import androidx.annotation.ColorInt
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.LatLngBounds
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.Polyline
import com.amap.api.maps2d.model.PolylineOptions
import java.util.ArrayList


internal abstract class RouteOverlay {

    protected abstract val mAMap: AMap

    abstract val startPoint: LatLng
    abstract val endPoint: LatLng

    protected abstract val routeWidth: Float

    @get:ColorInt
    protected abstract val routeColor: Int

    protected val mPolylineOptions: PolylineOptions by lazy {
        PolylineOptions().color(routeColor).width(routeWidth)
    }

    protected val stationMarkers: MutableList<Marker> = ArrayList()
    protected val allPolyLines: MutableList<Polyline> = ArrayList()

    @JvmField protected var nodeIconVisible = true

    abstract fun addToMap()

    /**
     * remove all markers from map
     */
    open fun removeFromMap() {
        allPolyLines.forEach { it.remove() }
    }

    fun zoomToSpan() {
        try {
            mAMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    getLatLngBounds(startPoint, endPoint),
                    50
                )
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected open fun getLatLngBounds(startPoint: LatLng, endPoint: LatLng): LatLngBounds {
        return LatLngBounds.builder()
            .include(LatLng(startPoint.latitude, startPoint.longitude))
            .include(LatLng(endPoint.latitude, endPoint.longitude))
            .build()
    }

    fun setNodeIconVisibility(visible: Boolean) {
        try {
            nodeIconVisible = visible
            stationMarkers.forEach { it.isVisible = visible }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected fun addStationMarker(options: MarkerOptions) {
        val marker = mAMap.addMarker(options)
        if (marker != null) {
            stationMarkers.add(marker)
        }
    }

    protected fun addPolyLine(options: PolylineOptions) {
        val polyline = mAMap.addPolyline(options)
        if (polyline != null) {
            allPolyLines.add(polyline)
        }
    }
}