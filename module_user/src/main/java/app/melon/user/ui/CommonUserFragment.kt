package app.melon.user.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.user.api.FollowerUserList
import app.melon.user.api.FollowingUserList
import app.melon.user.api.NearbyUserList
import app.melon.user.api.UserListConfig
import app.melon.user.ui.controller.UserPageController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


internal class CommonUserFragment : BasePagingListFragment() {

    private val pageConfig get() = requireArguments().getSerializable(KEY_PAGE_CONFIG) as UserListConfig

    @Inject internal lateinit var controllerFactory: UserPageController.Factory
    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { _, position -> "${listItemPrefix}_$position" },
            showFollowButton = false
        )
    }

    @Inject internal lateinit var viewModel: UserListViewModel

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.refresh(pageConfig)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refresh()
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.run {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                controller.submitData(it)
            }
        }
    }

    private val listItemPrefix: String
        get() {
            return when (pageConfig) {
                is FollowerUserList -> "followers"
                is FollowingUserList -> "following"
                NearbyUserList -> "nearby_user"
            }
        }

    companion object {
        private const val KEY_PAGE_CONFIG = "KEY_PAGE_CONFIG"

        internal fun newInstance(
            config: UserListConfig
        ) = CommonUserFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_PAGE_CONFIG, config)
            }
        }
    }
}