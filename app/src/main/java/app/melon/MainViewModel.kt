package app.melon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.melon.util.extensions.reverse
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private val _drawerMode = MutableLiveData<Boolean>()
    val drawerMode: LiveData<Boolean> = _drawerMode

    init {
        _drawerMode.value = true
    }

    fun drawerMenuChanged() {
        val state = _drawerMode.value!!
        _drawerMode.value = state.reverse()
    }
}