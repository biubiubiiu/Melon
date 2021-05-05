package app.melon.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.melon.base.framework.SingleEvent
import app.melon.util.extensions.reverse


internal class MainViewModel : ViewModel() {

    private val _drawerMode = MutableLiveData<Boolean>(true)
    internal val drawerMode: LiveData<Boolean> = _drawerMode

    private val _actionOpenDrawer = MutableLiveData<SingleEvent<Unit>>()
    internal val actionOpenDrawer: LiveData<SingleEvent<Unit>> = _actionOpenDrawer

    internal fun openDrawer() {
        _actionOpenDrawer.postValue(SingleEvent(Unit))
    }

    internal fun drawerMenuChanged() {
        val state = _drawerMode.value!!
        _drawerMode.value = state.reverse()
    }
}