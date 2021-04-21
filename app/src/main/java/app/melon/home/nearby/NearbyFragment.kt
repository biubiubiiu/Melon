package app.melon.home.nearby

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import app.melon.R
import app.melon.base.ui.databinding.CommonFragmentContainerBinding
import app.melon.location.LocateFail
import app.melon.location.LocateSuccess
import app.melon.location.LocationHelper
import app.melon.user.api.IUserService
import app.melon.user.api.NearbyUserList
import app.melon.util.delegates.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class NearbyFragment : DaggerFragment(R.layout.common_fragment_container) {

    private val binding: CommonFragmentContainerBinding by viewBinding()

    @Inject internal lateinit var userService: IUserService
    @Inject internal lateinit var locationHelper: LocationHelper

    @Inject internal lateinit var viewModel: NearbyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tryLocate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.locateResult.distinctUntilChanged().observe(viewLifecycleOwner, Observer {
            when (it) {
                is LocateSuccess -> showNearbyUsers(it.longitude, it.latitude)
                is LocateFail -> Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_LONG)
            }
        })
    }

    private fun showNearbyUsers(longitude: Double, latitude: Double) {
        if (childFragmentManager.findFragmentByTag(NEARBY_FRAGMENT_TAG) != null) {
            return
        }
        childFragmentManager.commit {
            add(
                R.id.fragment_container,
                userService.buildUserListFragment(
                    NearbyUserList(
                        listItemIdPrefix = "nearby_user",
                        longitude = longitude,
                        latitude = latitude
                    )
                ),
                NEARBY_FRAGMENT_TAG
            )
        }
    }

    companion object {
        private const val NEARBY_FRAGMENT_TAG = "NEARBY_FRAGMENT_TAG"
    }
}
