package app.melon.user.image

import android.app.Application
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
import app.melon.account.api.UserManager
import app.melon.base.framework.SingleEvent
import app.melon.user.interactor.SyncAvatarUpdate
import app.melon.user.interactor.UpdateAvatar
import kotlinx.coroutines.Dispatchers
import kotlin.Result
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


internal class ProfileImageViewModel constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val updateAvatar: UpdateAvatar,
    private val syncAvatarChange: SyncAvatarUpdate,
    private val userManager: UserManager
) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication()

    private val _avatarChangeEvent = MutableLiveData<SingleEvent<Uri>>()
    internal val avatarChangeEvent: LiveData<SingleEvent<Uri>> = _avatarChangeEvent

    /**
     * TakePicture activityResult action isn't returning the [Uri] once the image has been taken, so
     * we need to save the temporarily created URI in [savedStateHandle] until we handle its result
     */
    fun saveTemporarilyPhotoUri(uri: Uri?) {
        savedStateHandle["temporaryPhotoUri"] = uri
    }

    internal val temporaryPhotoUri: Uri?
        get() = savedStateHandle.get("temporaryPhotoUri")

    internal fun avatarChanged(uri: Uri) {
        _avatarChangeEvent.postValue(SingleEvent((uri)))
    }

    internal fun deleteTemporaryUri() {
        viewModelScope.launch {
            temporaryPhotoUri?.let {
                withContext(Dispatchers.IO) {
                    context.contentResolver.delete(it, null)
                }
                saveTemporarilyPhotoUri(null)
            }
        }
    }

    /**
     * create a [Uri] where the image will be stored
     */
    internal suspend fun createPhotoUri(): Uri? {
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

    private val _updateResult = MutableLiveData<Result<String>>()
    internal val updateResult: LiveData<Result<String>> = _updateResult

    private val _syncResult = MutableLiveData<Unit>()
    internal val syncResult: LiveData<Unit> = _syncResult

    init {
        viewModelScope.launch {
            updateAvatar.observe().collectLatest { result ->
                _updateResult.postValue(result)
            }
        }
    }

    internal fun checkIsMyProfile(uid: String): Boolean {
        return userManager.user?.id == uid
    }

    suspend fun syncAvatarUpdateToLocal(uid: String, url: String) {
        syncAvatarChange.updateAvatar(uid, url)
        _syncResult.postValue(Unit)
    }

    internal fun updateAvatar(file: File) {
        viewModelScope.launch {
            updateAvatar(UpdateAvatar.Params(file))
        }
    }
}

private fun generateFilename(extension: String = "jpg"): String {
    val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
    return "Melon_${currentDateTime}.$extension"
}