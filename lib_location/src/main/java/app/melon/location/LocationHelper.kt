package app.melon.location

import android.Manifest
import android.content.Context
import android.os.Build
import app.melon.util.extensions.addIf
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
import kotlin.coroutines.suspendCoroutine


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
    }

    private val locationClient = AMapLocationClient(context)
    private val locationOption = getDefaultOption()

    var lastLocation: SimplifiedLocation? = null
        private set

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

    suspend fun tryLocate(accuracyThreshold: Float = 100f): LocateResult {
        return suspendCoroutine { coroutine ->
            locationClient.setLocationListener {
                val result = it.toLocateResult()
                if (result is LocateSuccess) {
                    lastLocation = SimplifiedLocation(result.longitude, result.latitude)
                    if (result.accuracy > accuracyThreshold) {
                        return@setLocationListener // wait until next locating
                    }
                }
                locationClient.stopLocation()
                coroutine.resume(result)
            }
            locationClient.startLocation()
        }
    }

    fun calculateDistance(
        longitude1: Double,
        latitude1: Double,
        longitude2: Double,
        latitude2: Double
    ): Float {
        return AMapUtils.calculateLineDistance(LatLng(latitude1, longitude1), LatLng(latitude2, longitude2))
    }

    private fun AMapLocation?.toLocateResult(): LocateResult {
        return when {
            this == null -> LocateFail(
                -1,
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
                poiName = this.poiName
            )
        }
    }
}