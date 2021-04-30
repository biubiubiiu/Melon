package app.melon.composer.common

import android.app.Application
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


internal class AddMediaViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication()

    private val _currentMedia = MutableLiveData<MediaStoreImage>()
    val currentMedia: LiveData<MediaStoreImage> get() = _currentMedia

    /**
     * TakePicture activityResult action isn't returning the [Uri] once the image has been taken, so
     * we need to save the temporarily created URI in [savedStateHandle] until we handle its result
     */
    fun saveTemporarilyPhotoUri(uri: Uri?) {
        savedStateHandle["temporaryPhotoUri"] = uri
    }

    val temporaryPhotoUri: Uri?
        get() = savedStateHandle.get("temporaryPhotoUri")

    /**
     * [loadCameraMedia] is called when TakePicture intent is returning a
     * successful result
     */
    fun loadCameraMedia(uri: Uri) {
        loadImage(uri)
    }

    /**
     * create a [Uri] where the image will be stored
     */
    suspend fun createPhotoUri(): Uri? {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        return withContext(Dispatchers.IO) {
            val newImage = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, generateFilename())
            }

            // This method will perform a binder transaction which is better to execute off the main thread
            context.contentResolver.insert(imageCollection, newImage)
        }
    }

    /**
     * Performs a one shot load of images from [MediaStore.Images.Media.EXTERNAL_CONTENT_URI] into
     * the [_currentMedia] [LiveData] above.
     */
    fun loadImage(uri: Uri) {
        viewModelScope.launch {
            val media = queryImage(uri)
            if (media != null) {
                _currentMedia.postValue(media)
            }
        }
    }

    fun deleteTemporaryUri() {
        viewModelScope.launch {
            temporaryPhotoUri?.let {
                withContext(Dispatchers.IO) {
                    context.contentResolver.delete(it, null)
                }
                saveTemporarilyPhotoUri(null)
            }
        }
    }

    private suspend fun queryImage(uri: Uri): MediaStoreImage? {
        var queryResult: MediaStoreImage? = null

        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT
            )

            getApplication<Application>().contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

                if (cursor.moveToNext()) {

                    // Here we'll use the column indexes that we found above.
                    val id = cursor.getLong(idColumn)
                    val dateModified = Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    queryResult = MediaStoreImage(id, displayName, dateModified, width, height, contentUri)
                }
            }
        }
        return queryResult
    }
}

private fun generateFilename(extension: String = "jpg"): String {
    val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
    return "Melon_${currentDateTime}.$extension"
}
