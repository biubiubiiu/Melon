package app.melon.user

sealed class UserProfileAction {
    data class EnterUserProfile(val uid: String) : UserProfileAction()
}