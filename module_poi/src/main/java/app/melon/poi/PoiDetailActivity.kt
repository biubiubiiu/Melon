package app.melon.poi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.melon.base.ui.extensions.makeGone
import app.melon.base.ui.extensions.makeVisible
import app.melon.data.entities.PoiInfo
import app.melon.location.LocationHelper
import app.melon.location.SimplifiedLocation
import app.melon.location.toLatLng
import app.melon.location.toLatLonPoint
import app.melon.poi.data.location
import app.melon.poi.databinding.ActivityPoiDetailBinding
import app.melon.poi.databinding.ViewInfoWindowBinding
import app.melon.poi.ui.DaggerActivityWithMapView
import app.melon.poi.ui.PoiDetailViewModel
import app.melon.poi.ui.PoiDetailViewState
import app.melon.poi.ui.controller.PoiPageController
import app.melon.poi.ui.create
import app.melon.poi.ui.overlay.DrivingRouteOverLay
import app.melon.poi.ui.overlay.WalkRouteOverlay
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.viewModelProviderFactoryOf
import app.melon.util.formatter.MelonDateTimeFormatter
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.WalkPath
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


internal class PoiDetailActivity : DaggerActivityWithMapView(), AMap.InfoWindowAdapter {

    private val binding: ActivityPoiDetailBinding by viewBinding()
    private lateinit var infoWindowBinding: ViewInfoWindowBinding

    private val info get() = intent.getSerializableExtra(KEY_INFO) as PoiInfo

    override val mapView: MapView get() = binding.mapView

    @Inject internal lateinit var factory: PoiPageController.Factory
    private val controller by lazy { factory.create(this, ::shareRoute) }

    @Inject internal lateinit var viewModelFactory: PoiDetailViewModel.Factory
    private val viewModel: PoiDetailViewModel by viewModels() {
        viewModelProviderFactoryOf {
            viewModelFactory.create(
                info.poiId,
                info.location
            )
        }
    }

    @Inject internal lateinit var locationHelper: LocationHelper
    @Inject internal lateinit var formatter: MelonDateTimeFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initDataFlow()
    }

    override fun getInfoWindow(marker: Marker): View = infoWindowBinding.root

    override fun getInfoContents(marker: Marker): View = infoWindowBinding.root

    private fun initView() {
        initBottomSheet()
        initRecyclerView()
        initInfoWindow()
        setupMap()
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

    private fun initInfoWindow() {
        infoWindowBinding = ViewInfoWindowBinding.inflate(layoutInflater)
    }

    private fun setupMap() {
        aMap.setInfoWindowAdapter(this)
    }

    private fun initDataFlow() {
        lifecycleScope.launch {
            viewModel.feedsPagingData.collectLatest {
                controller.submitData(it)
            }
        }
        viewModel.selectObserve(PoiDetailViewState::location).observe(this, Observer {
            it ?: return@Observer
            it.origin?.let { position ->
                aMap.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .position(position.toLatLng())
                        .draggable(false)
                )
            }
            it.destination?.let { position ->
                val marker = aMap.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(position.toLatLng())
                        .title("").snippet("") // To make AMap SDK happy
                        .draggable(false)
                )
                marker.showInfoWindow()
            }
            if (it.origin != null && it.destination != null) {
                updateCameraPosition(it.origin, it.destination)
            }
        })
        viewModel.selectObserve(PoiDetailViewState::poiData).observe(this, Observer {
            controller.poiData = it
            if (it == null) {
                infoWindowBinding.skeleton.shimmerContainer.makeVisible()
                infoWindowBinding.contentRoot.makeGone()
                infoWindowBinding.skeleton.shimmerContainer.startShimmer()
            } else {
                infoWindowBinding.skeleton.shimmerContainer.stopShimmer()
                infoWindowBinding.skeleton.shimmerContainer.makeGone()
                infoWindowBinding.contentRoot.makeVisible()
                infoWindowBinding.name.text = it.name
                infoWindowBinding.address.text = it.address
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
        viewModel.selectObserve(PoiDetailViewState::refreshing).observe(this, Observer {
            binding.progressBar.isVisible = it
        })
    }

    private fun showWalkPathOverlay(walkPath: WalkPath) {
        val (myLocation, poiLocation) = viewModel.currentState().location ?: return
        if (myLocation != null && poiLocation != null) {
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
    }

    private fun showDrivePathOverlay(drivePath: DrivePath) {
        val (myLocation, poiLocation) = viewModel.currentState().location ?: return
        if (myLocation != null && poiLocation != null) {
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
    }

    private fun shareRoute(location: SimplifiedLocation) {
        viewModel.shareRoute(location)
    }

    companion object {
        private const val KEY_INFO = "KEY_INFO"

        internal fun start(context: Context, info: PoiInfo) {
            val intent = Intent(context, PoiDetailActivity::class.java).apply {
                putExtra(KEY_INFO, info)
            }
            context.startActivity(intent)
        }
    }
}