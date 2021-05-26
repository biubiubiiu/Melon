package app.melon.home.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import app.melon.databinding.FragmentNearbyBinding
import app.melon.home.base.HomepageToolbarFragment
import app.melon.user.api.IUserService
import app.melon.user.api.NearbyUserList
import javax.inject.Inject


class NearbyFragment : HomepageToolbarFragment<FragmentNearbyBinding>() {

    override val toolbar: Toolbar
        get() = binding.toolbar

    @Inject internal lateinit var userService: IUserService

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNearbyBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentNearbyBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        attachFragment()
    }

    private fun attachFragment() {
        if (childFragmentManager.findFragmentByTag(NEARBY_FRAGMENT_TAG) != null) {
            return
        }
        childFragmentManager.commit {
            add(
                binding.backbone.fragmentContainer.id,
                userService.buildUserListFragment(NearbyUserList),
                NEARBY_FRAGMENT_TAG
            )
        }
    }

    companion object {
        private const val NEARBY_FRAGMENT_TAG = "NEARBY_FRAGMENT_TAG"
    }
}
