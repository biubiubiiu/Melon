package app.melon.home.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import app.melon.databinding.FragmentNearbyBinding
import app.melon.home.base.HomepageToolbarFragment
import app.melon.location.LocateFail
import app.melon.location.LocateSuccess
import app.melon.user.api.IUserService
import app.melon.user.api.NearbyUserList
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class NearbyFragment : HomepageToolbarFragment<FragmentNearbyBinding>() {

    override val toolbar: Toolbar
        get() = binding.toolbar

    @Inject internal lateinit var viewModel: NearbyViewModel

    @Inject internal lateinit var userService: IUserService

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNearbyBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tryLocate()
    }

    override fun onViewCreated(binding: FragmentNearbyBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        viewModel.locateResult.distinctUntilChanged().observe(viewLifecycleOwner, Observer {
            when (it) {
                is LocateSuccess -> showNearbyUsers(it.longitude, it.latitude)
                is LocateFail -> Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun showNearbyUsers(longitude: Double, latitude: Double) {
        if (childFragmentManager.findFragmentByTag(NEARBY_FRAGMENT_TAG) != null) {
            return
        }
        childFragmentManager.commit {
            add(
                binding.backbone.fragmentContainer.id,
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
