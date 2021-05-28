package app.melon.user.image

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import app.melon.account.api.UserManager
import app.melon.user.interactor.SyncAvatarUpdate
import app.melon.user.interactor.UpdateAvatar
import app.melon.util.savestate.ViewModelFactory
import app.melon.util.storage.StorageHandler
import javax.inject.Inject

internal class ImageViewModelFactory @Inject constructor(
    private val application: Application,
    private val updateAvatar: UpdateAvatar,
    private val syncAvatarUpdate: SyncAvatarUpdate,
    private val storageHandler: StorageHandler,
    private val userManager: UserManager
) : ViewModelFactory<ProfileImageViewModel> {

    override fun create(handle: SavedStateHandle): ProfileImageViewModel {
        return ProfileImageViewModel(
            application,
            handle,
            updateAvatar,
            syncAvatarUpdate,
            storageHandler,
            userManager
        )
    }
}