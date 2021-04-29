package app.melon.poi.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.base.framework.SingleEvent
import app.melon.location.SimplifiedLocation
import app.melon.poi.interactor.ObserveMyLocation
import app.melon.poi.interactor.ShareRoute
import app.melon.poi.interactor.UpdateDriveRoute
import app.melon.poi.interactor.UpdatePoiDetail
import app.melon.poi.interactor.UpdatePoiFeeds
import app.melon.poi.interactor.UpdateWalkRoute
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


internal class PoiDetailViewModel @AssistedInject constructor(
    @Assisted initialState: PoiDetailViewState,
    private val observeMyLocation: ObserveMyLocation,
    private val updatePoiDetail: UpdatePoiDetail,
    private val updatePoiFeeds: UpdatePoiFeeds,
    private val updateWalkRoute: UpdateWalkRoute,
    private val updateDriveRoute: UpdateDriveRoute,
    private val shareRoute: ShareRoute
) : ReduxViewModel<PoiDetailViewState>(initialState) {

    val feedsPagingData = updatePoiFeeds.observe()

    init {
        viewModelScope.launch {
            observeMyLocation(Unit)
        }
        viewModelScope.launch {
            observeMyLocation.observe().collectAndSetState {
                copy(
                    location = OriginDestinationLocation(
                        origin = it,
                        destination = location?.destination
                    )
                )
            }
        }
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
        viewModelScope.launch {
            shareRoute.loadingState.observable.collectAndSetState {
                copy(refreshing = it)
            }
        }
        viewModelScope.launch {
            selectSubscribeDistinct(PoiDetailViewState::location).collectLatest { location ->
                location ?: return@collectLatest
                if (location.destination != null) {
                    updatePoiFeeds(UpdatePoiFeeds.Params(location.destination))
                }
                if (location.origin != null && location.destination != null) {
                    updateAndObserveWalkRoute(location.origin, location.destination)
                    updateAndObserveDriveRoute(location.origin, location.destination)
                }
            }
        }
        viewModelScope.launch {
            selectSubscribeDistinct(PoiDetailViewState::poiId).collectLatest { id ->
                updatePoiDetail(UpdatePoiDetail.Params(id))
            }
        }
    }

    private fun updateAndObserveDriveRoute(origin: SimplifiedLocation, destination: SimplifiedLocation) {
        viewModelScope.launch {
            updateDriveRoute.search(origin, destination)
                .collectAndSetState { result ->
                    when (result) {
                        is Success -> copy(drivePath = result.get())
                        is ErrorResult -> copy(error = result.throwable)
                    }
                }
        }
    }

    private fun updateAndObserveWalkRoute(origin: SimplifiedLocation, destination: SimplifiedLocation) {
        viewModelScope.launch {
            updateWalkRoute.search(origin, destination)
                .collectAndSetState { result ->
                    when (result) {
                        is Success -> copy(walkPath = result.get())
                        is ErrorResult -> copy(error = result.throwable)
                    }
                }
        }
    }

    internal fun shareRoute(dest: SimplifiedLocation) {
        val origin = currentState().location?.origin ?: return
        viewModelScope.launch {
            shareRoute(ShareRoute.Params(origin, dest))
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(initialState: PoiDetailViewState): PoiDetailViewModel
    }
}

internal fun PoiDetailViewModel.Factory.create(
    poiId: String,
    poiLocation: SimplifiedLocation? = null
): PoiDetailViewModel {
    return create(
        PoiDetailViewState(
            poiId = poiId,
            location = OriginDestinationLocation(
                destination = poiLocation
            )
        )
    )
}