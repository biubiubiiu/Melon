package app.melon.location

import android.content.Context
import app.melon.util.extensions.toException
import com.amap.api.services.core.AMapException
import com.amap.api.services.route.BusRouteResult
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.RouteSearch.FromAndTo
import com.amap.api.services.route.WalkPath
import com.amap.api.services.route.WalkRouteResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RouteSearchHelper @Inject constructor(
    context: Context
) {

    private val mCallbacks: ArrayList<RouteSearch.OnRouteSearchListener> = arrayListOf()
    private val routeSearchCallback = object : RouteSearch.OnRouteSearchListener {
        override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {
            mCallbacks.forEach { it.onBusRouteSearched(result, errorCode) }
        }

        override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
            mCallbacks.forEach { it.onDriveRouteSearched(result, errorCode) }
        }

        override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
            mCallbacks.forEach { it.onWalkRouteSearched(result, errorCode) }
        }

        override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {
            mCallbacks.forEach { it.onRideRouteSearched(result, errorCode) }
        }
    }

    private val mRouteSearch = RouteSearch(context)

    init {
        mRouteSearch.setRouteSearchListener(routeSearchCallback)
    }

    suspend fun searchWalkRoute(
        startPoint: SimplifiedLocation,
        endPoint: SimplifiedLocation
    ): Flow<Result<WalkPath?>> {
        val fromAndTo = FromAndTo(startPoint.toLatLonPoint(), endPoint.toLatLonPoint())
        val query = RouteSearch.WalkRouteQuery(fromAndTo)
        return getWalkRouteSearchResult().also {
            mRouteSearch.calculateWalkRouteAsyn(query)
        }
    }

    suspend fun searchDriveRoute(
        startPoint: SimplifiedLocation,
        endPoint: SimplifiedLocation
    ): Flow<Result<DrivePath?>> {
        val fromAndTo = FromAndTo(startPoint.toLatLonPoint(), endPoint.toLatLonPoint())
        val query = RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "")
        return getDriveRouteResult().also {
            mRouteSearch.calculateDriveRouteAsyn(query)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getWalkRouteSearchResult(): Flow<Result<WalkPath?>> = callbackFlow {
        val callback = object : RouteSearchListenerAdapter() {
            override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    offer(Result.success(result?.paths?.getOrNull(0)))
                } else {
                    offer(Result.failure("Search Route Error, error code: $errorCode".toException()))
                }
            }
        }
        mCallbacks.add(callback)
        awaitClose { mCallbacks.remove(callback) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getDriveRouteResult(): Flow<Result<DrivePath?>> = callbackFlow {
        val callback = object : RouteSearchListenerAdapter() {
            override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    offer(Result.success(result?.paths?.getOrNull(0)))
                } else {
                    offer(Result.failure("Search Route Error, error code: $errorCode".toException()))
                }
            }
        }
        mCallbacks.add(callback)
        awaitClose { mCallbacks.remove(callback) }
    }
}