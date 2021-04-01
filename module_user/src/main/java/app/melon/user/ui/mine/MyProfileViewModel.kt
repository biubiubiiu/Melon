package app.melon.user.ui.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.melon.data.entities.User
import app.melon.user.interactor.UpdateUserFeeds
import javax.inject.Inject

class MyProfileViewModel @Inject constructor(
    // TODO inject UserManager
    private val updateMyFeeds: UpdateUserFeeds,
    private val updateMyAnonymousFeeds: UpdateUserFeeds,
    private val updateMyFavorFeeds: UpdateUserFeeds
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    val myFeedsPagingData = updateMyFeeds.observe()
    val myAnonymousFeedsPagingData = updateMyAnonymousFeeds.observe()
    val myFavorFeedsPagingData = updateMyFavorFeeds.observe()

    init {
        _user.value = User(
            id = "test",
            username = "Raymond Wong",
            school = "Cambridge Unveristy",
            avatarUrl = "https://bkimg.cdn.bcebos.com/pic/9d82d158ccbf6c814557c0e2b03eb13533fa405c?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5/format,f_auto",
            backgroundUrl = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1370510529,3755695703&fm=26&gp=0.jpg",
            followerCount = 100,
            followingCount = 1002
        )

        setUid("6")
    }

    fun reselect(position: Int) {

    }

    // TODO retrieve uid from UserManager
    // TODO add type as param to UpdateUserFeeds
    fun setUid(uid: String) {
        updateMyFeeds(UpdateUserFeeds.Params(uid = uid))
        updateMyAnonymousFeeds(UpdateUserFeeds.Params(uid = uid))
        updateMyFavorFeeds(UpdateUserFeeds.Params(uid = uid))
    }
}