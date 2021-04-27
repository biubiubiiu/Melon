package app.melon.poi.ui.overlay

import androidx.annotation.ColorInt
import app.melon.poi.R
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.BitmapDescriptor
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.LatLngBounds
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.Polyline
import com.amap.api.maps2d.model.PolylineOptions
import java.util.ArrayList

abstract class RouteOverlay {

    protected abstract val mAMap: AMap

    abstract val startPoint: LatLng
    abstract val endPoint: LatLng

    protected abstract val routeWidth: Float

    @get:ColorInt
    protected abstract val routeColor: Int

    protected val mPolylineOptions: PolylineOptions by lazy {
        PolylineOptions().color(routeColor).width(routeWidth)
    }

    protected open val startBitmapDescriptor: BitmapDescriptor
        get() = BitmapDescriptorFactory.fromResource(R.drawable.amap_start)

    protected open val endBitmapDescriptor: BitmapDescriptor
        get() = BitmapDescriptorFactory.fromResource(R.drawable.amap_end)

    protected val stationMarkers: MutableList<Marker> = ArrayList()
    protected val allPolyLines: MutableList<Polyline> = ArrayList()

    protected var startMarker: Marker? = null
    protected var endMarker: Marker? = null

    @JvmField protected var nodeIconVisible = true

    abstract fun addToMap()

    /**
     * remove all markers from map
     */
    open fun removeFromMap() {
        startMarker?.remove()
        endMarker?.remove()
        allPolyLines.forEach { it.remove() }
    }

    protected open fun addStartAndEndMarker() {
        startMarker = mAMap.addMarker(MarkerOptions()
            .position(startPoint)
            .icon(startBitmapDescriptor)
            .title("\u8D77\u70B9")
        )

        endMarker = mAMap.addMarker(MarkerOptions()
            .position(endPoint)
            .icon(endBitmapDescriptor)
            .title("\u7EC8\u70B9")
        )
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