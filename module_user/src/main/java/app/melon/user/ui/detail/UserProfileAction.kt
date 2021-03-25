package app.melon.user.ui.detail

sealed class UserProfileAction {
    data class EnterUserProfile(val uid: String) : UserProfileAction()
    data class FetchFirstPageUserFeeds(val uid: String): UserProfileAction()
}