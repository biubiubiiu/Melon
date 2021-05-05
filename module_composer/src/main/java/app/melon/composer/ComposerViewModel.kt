package app.melon.composer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import app.melon.base.framework.SingleEvent
import app.melon.composer.common.MediaStoreImage
import app.melon.data.entities.PoiInfo
import app.melon.util.extensions.or
import java.util.Collections


internal class ComposerViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _actionSubmit = MutableLiveData<SingleEvent<Unit>>()
    internal val actionSubmit: LiveData<SingleEvent<Unit>> = _actionSubmit

    internal fun submitAndLeave() {
        _actionSubmit.postValue(SingleEvent(Unit))
    }

    private val _actionLeave = MutableLiveData<SingleEvent<Unit>>()
    internal val actionLeave: LiveData<SingleEvent<Unit>> = _actionLeave

    internal fun leave() {
        _actionLeave.postValue(SingleEvent(Unit))
    }

    private val _avatarUrl = MutableLiveData<String>()
    val avatarUrl: LiveData<String> get() = _avatarUrl

    fun updateAvatarUrl(url: String?) {
        _avatarUrl.takeIf { url != null }?.postValue(url)
    }

    private val _images = MutableLiveData<List<MediaStoreImage>>(emptyList())
    val images: LiveData<List<MediaStoreImage>> get() = _images

    fun updateSelection(images: List<MediaStoreImage>) {
        _images.postValue(images)
    }

    fun deselectImage(image: MediaStoreImage) {
        val curr = images.value ?: emptyList()
        _images.postValue(curr - image)
    }

    fun swapImagePosition(from: Int, to: Int): Boolean {
        val result = mutableListOf<MediaStoreImage>()
        result.addAll(images.value ?: emptyList())
        Collections.swap(result, from, to)
        _images.value = result
        return true
    }

    private val _textContent = MutableLiveData<String>("")
    internal val textContent: LiveData<String> = _textContent

    val textInputProgress = _textContent.map {
        if (it.isBlank()) {
            0
        } else {
            it.length * 100 / MAX_INPUT_LENGTH
        }
    }

    fun contentChanged(text: String) {
        _textContent.postValue(text)
    }

    private val textValid = _textContent.map { it.isNotBlank() && it.length <= MAX_INPUT_LENGTH }
    private val imagesValid = images.map { it.isNotEmpty() }

    val inputValid = textValid.or(imagesValid)

    private val _locationInfo = MutableLiveData<PoiInfo?>(null)
    val locationInfo: LiveData<PoiInfo?> = _locationInfo

    fun locationChanged(selection: PoiInfo?) {
        _locationInfo.postValue(selection)
    }

    companion object {
        internal const val MAX_INPUT_LENGTH = 200
    }
}