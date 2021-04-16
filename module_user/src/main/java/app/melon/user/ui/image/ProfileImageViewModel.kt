package app.melon.user.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.melon.user.interactor.UpdateAvatar
import app.melon.util.base.Result
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class ProfileImageViewModel @Inject constructor(
    private val updateAvatar: UpdateAvatar
) : ViewModel() {

    private val _updateResult = MutableLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> = _updateResult

    private val _avatarUrl = MutableLiveData<String>()
    val avatarUrl: LiveData<String> = _avatarUrl

    init {
        viewModelScope.launch {
            updateAvatar.observe().collectLatest {
                _updateResult.postValue(it)
            }
        }
    }

    fun updateAvatarUrl(url: String) {
        _avatarUrl.postValue(url)
    }

    fun updateAvatar(file: File) {
        viewModelScope.launch {
            updateAvatar(UpdateAvatar.Params(file))
        }
    }
}