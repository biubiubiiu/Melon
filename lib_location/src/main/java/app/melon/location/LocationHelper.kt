package app.melon.location

import android.Manifest
import android.content.Context
import android.os.Build
import app.melon.util.extensions.addIf
import app.melon.util.extensions.suspendCoroutineWithTimeout
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol
import com.amap.api.maps2d.AMapUtils
import com.amap.api.maps2d.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume


@Singleton
class LocationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        ).addIf(
            predicate = { Build.VERSION.SDK_INT < Build.VERSION_CODES.P },
            element = Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).toTypedArray()

        private const val CUSTOM_ERROR_CODE = -1
    }

    private val locationClient = AMapLocationClient(context)
    private val locationOption = getDefaultOption()

    var lastLocation: SimplifiedLocation? = null
        private set

    private var lastSuccessfulLocate: LocateSuccess? = null

    init {
        initLocation()
    }

    private fun initLocation() {
        locationClient.setLocationOption(locationOption)
    }

    private fun getDefaultOption(): AMapLocationClientOption {
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTPS)
        return AMapLocationClientOption().apply {
            locationMode = AMapLocationMode.Hight_Accuracy
            isGpsFirst = true
            httpTimeOut = 30000
            interval = 2000
            isNeedAddress = true
            isOnceLocation = false
            isOnceLocationLatest = false
            isSensorEnable = false
            isWifiScan = true
            isLocationCacheEnable = true
            geoLanguage = AMapLocationClientOption.GeoLanguage.DEFAULT
        }
    }

    suspend fun getLocation(useHistoryLocation: Boolean = true): SimplifiedLocation? {
        lastSuccessfulLocate?.let {
            if (useHistoryLocation && !isLocateResultOutdated(it.time)) {
                return it.location
            }
        }
        return when (val locateResult = tryLocate()) {
            is LocateSuccess -> locateResult.location
            is LocateFail -> null
        }
    }

    private fun isLocateResultOutdated(lastLocateTime: Long): Boolean {
        val gap = System.currentTimeMillis() - lastLocateTime
        return gap >= 0 && gap <= 1800 * 1000
    }

    suspend fun tryLocate(
        accuracyThreshold: Float = 100f,
        timeoutMillis: Long = 5000L
    ): LocateResult {
        return suspendCoroutineWithTimeout(timeoutMillis) { coroutine ->
            locationClient.setLocationListener {
                val result = it.toLocateResult()
                if (result is LocateSuccess) {
                    recordSuccessfulLocate(result)
                    if (result.accuracy > accuracyThreshold) {
                        return@setLocationListener // wait until next locating
                    }
                }
                locationClient.stopLocation()
                coroutine.resume(result)
            }
            locationClient.startLocation()
        } ?: LocateFail(CUSTOM_ERROR_CODE, "Locate timeout").also {
            locationClient.stopLocation() // make sure it stops locating after timeout
        }
    }

    private fun recordSuccessfulLocate(locate: LocateSuccess) {
        lastSuccessfulLocate = locate
        lastLocation = SimplifiedLocation(locate.longitude, locate.latitude)
    }

    fun calculateDistance(
        longitude1: Double,
        latitude1: Double,
        longitude2: Double,
        latitude2: Double
    ): Float {
        return AMapUtils.calculateLineDistance(LatLng(latitude1, longitude1), LatLng(latitude2, longitude2))
    }

    fun getMyDistanceTo(
        longitude: Double,
        latitude: Double
    ): Float? {
        val myLocation = lastLocation ?: return null
        return calculateDistance(
            myLocation.longitude,
            myLocation.latitude,
            longitude,
            latitude
        )
    }

    private fun AMapLocation?.toLocateResult(): LocateResult {
        return when {
            this == null -> LocateFail(
                CUSTOM_ERROR_CODE,
                context.getString(R.string.locate_fail_null_result)
            )
            this.errorCode == 12 -> LocateFail(
                this.errorCode,
                context.getString(R.string.locate_fail_missing_permission)
            )
            this.errorCode != 0 -> LocateFail(
                this.errorCode,
                this.errorInfo
            )
            else -> LocateSuccess(
                longitude = this.longitude,
                latitude = this.latitude,
                accuracy = this.accuracy,
                poiName = this.poiName,
                time = this.time
            )
        }
    }
}