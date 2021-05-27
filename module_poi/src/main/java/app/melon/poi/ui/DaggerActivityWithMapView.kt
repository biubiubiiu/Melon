package app.melon.poi.ui

import android.os.Bundle
import androidx.annotation.CallSuper
import app.melon.location.SimplifiedLocation
import app.melon.location.toLatLng
import app.melon.util.extensions.dpInt
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.AMapOptions
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.UiSettings
import com.amap.api.maps2d.model.LatLngBounds
import dagger.android.support.DaggerAppCompatActivity


internal abstract class DaggerActivityWithMapView : DaggerAppCompatActivity() {

    internal abstract val mapView: MapView
    protected val aMap: AMap get() = mapView.map

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        initMapViewSettings(aMap.uiSettings)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    open fun initMapViewSettings(settings: UiSettings) {
        settings.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT
        settings.isCompassEnabled = false
        settings.isZoomControlsEnabled = false
        settings.isScaleControlsEnabled = false
        settings.isMyLocationButtonEnabled = false
    }

    protected fun updateCameraPosition(myLocation: SimplifiedLocation, destLocation: SimplifiedLocation) {
        val bound = LatLngBounds.Builder()
            .include(myLocation.toLatLng())
            .include(destLocation.toLatLng())
            .build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bound, 48.dpInt)
        aMap.moveCamera(cameraUpdate)
    }
}