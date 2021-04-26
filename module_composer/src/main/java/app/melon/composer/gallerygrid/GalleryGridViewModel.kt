package app.melon.composer.gallerygrid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.melon.composer.common.MediaStoreImage


internal class GalleryGridViewModel : ViewModel() {

    private val _selection = MutableLiveData<List<MediaStoreImage>>()
    val selections: LiveData<List<MediaStoreImage>> get() = _selection

    fun syncSelection(images: List<MediaStoreImage>) {
        _selection.postValue(images)
    }

    fun selectImage(image: MediaStoreImage) {
        val curr = selections.value ?: emptyList()
        _selection.postValue(curr + image)
    }

    fun deselectImage(image: MediaStoreImage) {
        val curr = selections.value ?: emptyList()
        _selection.postValue(curr - image)
    }
}

