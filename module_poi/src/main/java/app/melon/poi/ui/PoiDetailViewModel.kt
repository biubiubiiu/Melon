package app.melon.poi.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.base.framework.SingleEvent
import app.melon.location.SimplifiedLocation
import app.melon.poi.interactor.ShareRoute
import app.melon.poi.interactor.UpdateDriveRoute
import app.melon.poi.interactor.UpdatePoiDetail
import app.melon.poi.interactor.UpdatePoiFeeds
import app.melon.poi.interactor.UpdateWalkRoute
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import kotlinx.coroutines.launch
import javax.inject.Inject


class PoiDetailViewModel @Inject constructor(
    private val updatePoiDetail: UpdatePoiDetail,
    private val updatePoiFeeds: UpdatePoiFeeds,
    private val updateWalkRoute: UpdateWalkRoute,
    private val updateDriveRoute: UpdateDriveRoute,
    private val shareRoute: ShareRoute
) : ReduxViewModel<PoiDetailViewState>(
    initialState = PoiDetailViewState()
) {

    val feedsPagingData = updatePoiFeeds.observe()

    init {
        viewModelScope.launch {
            updatePoiDetail.observe().collectAndSetState {
                when (it) {
                    is Success -> copy(poiData = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
        viewModelScope.launch {
            shareRoute.observe().collectAndSetState {
                when (it) {
                    is Success -> copy(shareRoute = SingleEvent(it.get()))
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
    }

    fun refresh(poiId: String, start: SimplifiedLocation, dest: SimplifiedLocation) {
        viewModelScope.launch {
            updatePoiDetail(UpdatePoiDetail.Params(poiId))
        }
        viewModelScope.launch {
            updatePoiFeeds(UpdatePoiFeeds.Params(dest))
        }
        viewModelScope.launch {
            updateWalkRoute.search(start, dest).collectAndSetState {
                when (it) {
                    is Success -> copy(walkPath = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
        viewModelScope.launch {
            updateDriveRoute.search(start, dest).collectAndSetState {
                when (it) {
                    is Success -> copy(drivePath = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
    }

    fun shareRoute(start: SimplifiedLocation, dest: SimplifiedLocation) {
        viewModelScope.launch {
            shareRoute(ShareRoute.Params(start, dest))
        }
    }
}