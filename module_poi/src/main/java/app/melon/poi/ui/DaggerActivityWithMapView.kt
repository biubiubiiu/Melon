package app.melon.poi.ui

import android.os.Bundle
import androidx.annotation.CallSuper
import com.amap.api.maps2d.AMapOptions
import com.amap.api.maps2d.CameraUpdate
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.UiSettings
import com.amap.api.maps2d.model.CameraPosition
import dagger.android.support.DaggerAppCompatActivity


abstract class DaggerActivityWithMapView : DaggerAppCompatActivity() {

    abstract val mapView: MapView
    protected val aMap get() = mapView.map

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        initMapViewSettings(aMap.uiSettings)
        initCameraPosition()
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

    private fun initCameraPosition() {
        val cameraUpdate = getDefaultCameraPosition() ?: return
        aMap.moveCamera(cameraUpdate)
    }

    abstract fun getDefaultCameraPosition(): CameraUpdate?
}