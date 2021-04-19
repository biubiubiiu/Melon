package app.melon.poi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.melon.location.LocationHelper
import app.melon.location.SimplifiedLocation
import app.melon.location.toLatLng
import app.melon.location.toLatLonPoint
import app.melon.poi.databinding.ActivityPoiDetailBinding
import app.melon.poi.ui.DaggerActivityWithMapView
import app.melon.poi.ui.PoiDetailViewModel
import app.melon.poi.ui.PoiDetailViewState
import app.melon.poi.ui.controller.PoiPageController
import app.melon.poi.ui.overlay.DrivingRouteOverLay
import app.melon.poi.ui.overlay.WalkRouteOverlay
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.dpInt
import app.melon.util.formatter.MelonDateTimeFormatter
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdate
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLngBounds
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.WalkPath
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class PoiDetailActivity : DaggerActivityWithMapView(), AMap.InfoWindowAdapter {

    private val binding: ActivityPoiDetailBinding by viewBinding()

    private val infoWindow by lazy {
        layoutInflater.inflate(R.layout.view_info_window, binding.root, false)
    }

    override val mapView: MapView get() = binding.mapView

    @Inject internal lateinit var factory: PoiPageController.Factory
    private val controller by lazy { factory.create(this, ::shareRoute) }

    @Inject internal lateinit var viewModel: PoiDetailViewModel
    @Inject internal lateinit var locationHelper: LocationHelper
    @Inject internal lateinit var formatter: MelonDateTimeFormatter

    private val poiLocation = SimplifiedLocation(
        longitude = 114.1808934593,
        latitude = 22.322230460245
    )

    private val myLocation by lazy {
        locationHelper.lastLocation ?: SimplifiedLocation(
            longitude = 114.419825,
            latitude = 30.518659
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initDataFlow()
        refresh()
    }

    override fun getDefaultCameraPosition(): CameraUpdate? {
        return CameraUpdateFactory.newLatLngBounds(
            LatLngBounds(poiLocation.toLatLng(), myLocation.toLatLng()),
            48.dpInt
        )
    }

    override fun getInfoWindow(marker: Marker): View = infoWindow

    override fun getInfoContents(marker: Marker): View = infoWindow

    private fun initView() {
        initBottomSheet()
        initRecyclerView()
        setupMap()
        addMarkersToMap()
    }

    private fun initBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.mainBottomsheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.halfExpandedRatio = 0.01f // skip half-expand state
        behavior.peekHeight = 320.dpInt
        behavior.isHideable = false
    }

    private fun initRecyclerView() {
        binding.recyclerView.setController(controller)
    }

    private fun setupMap() {
        aMap.setInfoWindowAdapter(this)
    }

    private fun addMarkersToMap() {
        myLocation.let {
            aMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(it.toLatLng())
                .draggable(false))
        }
        poiLocation.let {
            val marker = aMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(it.toLatLng())
                .title("").snippet("") // To make AMap SDK happy
                .draggable(false))
            marker.showInfoWindow()
        }
    }

    private fun initDataFlow() {
        lifecycleScope.launch {
            viewModel.feedsPagingData.collectLatest {
                controller.submitData(it)
            }
        }
        viewModel.selectObserve(PoiDetailViewState::poiData).observe(this, Observer {
            controller.poiData = it
            if (it != null) {
                infoWindow.findViewById<TextView>(R.id.name).text = it.name
                infoWindow.findViewById<TextView>(R.id.address).text = it.address
            }
        })
        viewModel.selectObserve(PoiDetailViewState::walkPath).observe(this, Observer {
            binding.walkDuration.isVisible = it != null
            if (it != null) {
                binding.walkDuration.text =
                    getString(R.string.walk_route_duration, formatter.formatDuration(it.duration))
                binding.walkDuration.setOnClickListener { view ->
                    view.isSelected = true
                    binding.driveDuration.isSelected = false
                    showWalkPathOverlay(it)
                }
            }
        })
        viewModel.selectObserve(PoiDetailViewState::drivePath).observe(this, Observer {
            binding.driveDuration.isVisible = it != null
            if (it != null) {
                binding.driveDuration.text =
                    getString(R.string.drive_route_duration, formatter.formatDuration(it.duration))
                binding.driveDuration.setOnClickListener { view ->
                    view.isSelected = true
                    binding.walkDuration.isSelected = false
                    showDrivePathOverlay(it)
                }
            }
        })
        viewModel.selectObserve(PoiDetailViewState::shareRoute).observe(this, Observer {
            it?.getContentIfNotHandled()?.let { url ->
                binding.webView.loadUrl(url)
            }
        })
    }

    private fun showWalkPathOverlay(walkPath: WalkPath) {
        val walkRouteOverlay = WalkRouteOverlay(
            aMap,
            walkPath,
            myLocation.toLatLonPoint(),
            poiLocation.toLatLonPoint()
        )
        walkRouteOverlay.removeFromMap()
        walkRouteOverlay.addToMap()
        walkRouteOverlay.zoomToSpan()
    }

    private fun showDrivePathOverlay(drivePath: DrivePath) {
        val drivingRouteOverlay = DrivingRouteOverLay(
            aMap,
            drivePath,
            myLocation.toLatLonPoint(),
            poiLocation.toLatLonPoint()
        )
        drivingRouteOverlay.setNodeIconVisibility(false)
        drivingRouteOverlay.isColorFullLine = false
        drivingRouteOverlay.removeFromMap()
        drivingRouteOverlay.addToMap()
        drivingRouteOverlay.zoomToSpan()
    }

    private fun refresh() {
        viewModel.refresh("B001B0ISPB", myLocation, poiLocation) // TODO
    }

    private fun shareRoute(location: SimplifiedLocation) {
        viewModel.shareRoute(myLocation, location)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PoiDetailActivity::class.java))
        }
    }
}