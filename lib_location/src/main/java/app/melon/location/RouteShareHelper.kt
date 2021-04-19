package app.melon.location

import android.content.Context
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.toException
import com.amap.api.services.core.AMapException
import com.amap.api.services.share.ShareSearch
import com.amap.api.services.share.ShareSearch.ShareDrivingRouteQuery
import com.amap.api.services.share.ShareSearch.ShareFromAndTo
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class RouteShareHelper @Inject constructor(
    context: Context
) {

    private val mShareSearch = ShareSearch(context)

    suspend fun shareRoute(start: SimplifiedLocation, dest: SimplifiedLocation): Result<String?> {
        return suspendCoroutine {
            val fromAndTo = ShareFromAndTo(start.toLatLonPoint(), dest.toLatLonPoint())
            val query = ShareDrivingRouteQuery(fromAndTo, ShareSearch.DrivingDefault)

            mShareSearch.setOnShareSearchListener(object : ShareSearchListenerAdapter() {
                override fun onDrivingRouteShareUrlSearched(url: String?, errorCode: Int) {
                    handleShareResult(it, url, errorCode)
                }
            })
            mShareSearch.searchDrivingRouteShareUrlAsyn(query)
        }
    }

    private fun handleShareResult(continuation: Continuation<Result<String?>>, url: String?, errorCode: Int) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            continuation.resume(Success(url))
        } else {
            continuation.resume(ErrorResult("Share Fail with error code: $errorCode".toException()))
        }
    }
}