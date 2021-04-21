package app.melon.user.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.user.api.NearbyUserList
import app.melon.user.api.UserListConfig
import app.melon.user.ui.controller.UserPageController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class CommonUserFragment : BasePagingListFragment() {

    private val pageConfig by lazy { requireArguments().getSerializable(KEY_PAGE_CONFIG) as UserListConfig }

    @Inject internal lateinit var controllerFactory: UserPageController.Factory
    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { _, position -> "${pageConfig.listItemIdPrefix}_$position" },
            showFollowButton = false
        )
    }

    @Inject internal lateinit var viewModel: UserListViewModel

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            val dataFlow = when (val config = pageConfig) {
                is NearbyUserList -> viewModel.fetchNearbyUser(
                    config.longitude,
                    config.latitude
                )
                else -> viewModel.refresh()
            }
            dataFlow.collectLatest {
                controller.submitData(it)
            }
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