package app.melon.home.nearby

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import app.melon.R
import app.melon.base.ui.databinding.CommonFragmentContainerBinding
import app.melon.location.LocateFail
import app.melon.location.LocateSuccess
import app.melon.location.LocationHelper
import app.melon.permission.PermissionHelperOwner
import app.melon.user.api.IUserService
import app.melon.user.api.NearbyUserList
import app.melon.util.delegates.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class NearbyFragment : DaggerFragment(R.layout.common_fragment_container) {

    private val binding: CommonFragmentContainerBinding by viewBinding()

    @Inject internal lateinit var userService: IUserService
    @Inject internal lateinit var locationHelper: LocationHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tryLocateAndShowNearbyUsers()
    }

    private fun tryLocateAndShowNearbyUsers() {
        (activity as? PermissionHelperOwner)?.checkPermission(LocateRequest) {
            lifecycleScope.launchWhenStarted {
                val result = withContext(Dispatchers.IO) {
                    locationHelper.tryLocate()
                }
                when (result) {
                    is LocateSuccess -> showNearbyUsers(result.longitude, result.latitude)
                    is LocateFail -> Snackbar.make(binding.root, result.errorMessage, Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    private fun showNearbyUsers(longitude: Double, latitude: Double) {
        childFragmentManager.commit {
            replace(
                R.id.fragment_container,
                userService.buildUserListFragment(
                    NearbyUserList(
                        listItemIdPrefix = "nearby_user",
                        longitude = longitude,
                        latitude = latitude
                    )
                )
            )
        }
    }
}
