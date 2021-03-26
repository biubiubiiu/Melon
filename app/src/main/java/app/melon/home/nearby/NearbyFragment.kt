package app.melon.home.nearby

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.data.entities.User
import app.melon.user.api.IUserService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class NearbyFragment : BasePagingListFragment() {

    @Inject lateinit var nearbyViewModel: NearbyViewModel

    @Inject lateinit var userService: IUserService

    override val controller by lazy(LazyThreadSafetyMode.NONE) {
        NearbyPageController(requireContext()).apply {
            callbacks = object : NearbyPageController.Actions {
                override fun onHolderClick(user: User?) {
                    val uid = user?.id ?: return
                    userService.navigateToUserProfile(requireContext(), uid)
                }
            }
        }
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        fetchData()
        setHasOptionsMenu(true)
        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            nearbyViewModel.getStream().collectLatest {
                controller.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.clear()
    }

    override fun invalidate() {
        // No-op
    }
}
