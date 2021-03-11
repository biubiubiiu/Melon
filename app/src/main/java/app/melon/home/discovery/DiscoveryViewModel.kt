package app.melon.home.discovery

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.PersistState

data class DiscoveryState(
    @PersistState val page: Int = 0,
    val reselectPage: Int? = null
) : MavericksState

class DiscoveryViewModel(initialState: DiscoveryState) : MavericksViewModel<DiscoveryState>(initialState) {

    fun reselect(page: Int) = setState {
        copy(reselectPage = page)
    }

    fun onReselectionHandled() = setState {
        copy(reselectPage = null)
    }
}