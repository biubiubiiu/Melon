package app.melon.home.nearby

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import app.melon.R
import app.melon.databinding.FragmentNearbyBinding
import app.melon.home.MainViewModel
import app.melon.location.LocateFail
import app.melon.location.LocateSuccess
import app.melon.user.api.IUserService
import app.melon.user.api.NearbyUserList
import app.melon.util.delegates.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class NearbyFragment : DaggerFragment(R.layout.fragment_nearby) {

    private val binding: FragmentNearbyBinding by viewBinding()

    private val mainViewModel: MainViewModel by activityViewModels()
    @Inject internal lateinit var viewModel: NearbyViewModel

    @Inject internal lateinit var userService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tryLocate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.locateResult.distinctUntilChanged().observe(viewLifecycleOwner, Observer {
            when (it) {
                is LocateSuccess -> showNearbyUsers(it.longitude, it.latitude)
                is LocateFail -> Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_LONG).show()
            }
        })
        if (savedInstanceState == null) {
            setupView()
        }
    }

    private fun setupView() {
        setupToolbar()
    }

    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            mainViewModel.openDrawer()
        }
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
