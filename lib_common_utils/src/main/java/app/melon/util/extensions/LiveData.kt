package app.melon.util.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun LiveData<Boolean>.or(other: LiveData<Boolean>): LiveData<Boolean> {
    return MediatorLiveData<Boolean>().apply {
        var lastThis = this.value ?: false
        var lastOther = other.value ?: false

        fun update() {
            val localThis = lastThis
            val localOther = lastOther
            this.value = localThis || localOther
        }

        addSource(other) {
            lastOther = it
            update()
        }

        addSource(this@or) {
            lastThis = it
            update()
        }
    }
}