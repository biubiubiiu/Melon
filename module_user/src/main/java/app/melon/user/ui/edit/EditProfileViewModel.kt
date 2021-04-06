package app.melon.user.ui.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.data.entities.User
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
) : ReduxViewModel<EditProfileViewState>(
    initialState = EditProfileViewState()
) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        _user.value = User(
            id = "test",
            username = "Raymond Wong",
            school = "Cambridge Unveristy",
            avatarUrl = "https://bkimg.cdn.bcebos.com/pic/9d82d158ccbf6c814557c0e2b03eb13533fa405c?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5/format,f_auto",
            backgroundUrl = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
            followerCount = 100,
            followingCount = 1002,
            photos = listOf(
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg"
            )
        )
        viewModelScope.launchSetState {
            copy(
                pageUser = User(
                    id = "test",
                    username = "Raymond Wong",
                    school = "Cambridge Unveristy",
                    avatarUrl = "https://bkimg.cdn.bcebos.com/pic/9d82d158ccbf6c814557c0e2b03eb13533fa405c?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5/format,f_auto",
                    backgroundUrl = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                    followerCount = 100,
                    followingCount = 1002,
                    photos = listOf(
                        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
                        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg"
                    )
                )
            )
        }
    }

    fun save() {
        Log.d("raymond", "save with ${currentState().pageUser}")
    }

    fun hometownChanged(hometown: String) {
        updatePageUser {
            copy(hometown = hometown)
        }
    }

    fun collegeChanged(college: String) {
        updatePageUser {
            copy(college = college)
        }
    }

    fun majorChanged(major: String) {
        updatePageUser {
            copy(major = major)
        }
    }

    fun degreeChanged(degree: String) {
        updatePageUser {
            copy(degree = degree)
        }
    }

    fun descriptionChanged(bio: String) {
        updatePageUser {
            copy(description = bio)
        }
    }

    fun photoListChanged(list: List<String>) {
        updatePageUser {
            copy(photos = list)
        }
    }

    private fun updatePageUser(reducer: User.() -> User) {
        viewModelScope.launchSetState {
            copy(
                pageUser = reducer(pageUser!!)
            )
        }
    }
}