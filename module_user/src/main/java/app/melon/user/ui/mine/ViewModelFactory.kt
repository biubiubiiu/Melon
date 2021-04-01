package app.melon.user.ui.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.melon.user.interactor.UpdateUserFeeds
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton


@Singleton
internal class ViewModelFactory @Inject constructor(
    private val updateUserFeeds: Provider<UpdateUserFeeds>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(MyProfileViewModel::class.java) -> MyProfileViewModel(
                updateUserFeeds.get(),
                updateUserFeeds.get(),
                updateUserFeeds.get()
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}