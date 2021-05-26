package app.melon.user.nearby

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BaseEpoxyListFragment
import app.melon.user.ui.controller.UserPageController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


internal class NearbyUserListFragment : BaseEpoxyListFragment() {

    @Inject internal lateinit var viewModel: NearbyViewModel

    @Inject internal lateinit var controllerFactory: UserPageController.Factory
    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { _, position -> "${LIST_ITEM_PREFIX}_$position" },
            showFollowButton = false
        )
    }

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refresh()
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.also { v ->
            v.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        controller.addLoadStateListener {
            binding.swipeRefreshLayout.post {
                binding.swipeRefreshLayout.isRefreshing = it.refresh !is LoadState.NotLoading
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        lifecycleScope.launch {
            viewModel.locating.observable.collectLatest { locating ->
                binding.swipeRefreshLayout.isRefreshing = locating
            }
        }
        viewModel.locateFail.observe(viewLifecycleOwner, Observer { errorMessage ->
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        })
        viewModel.dataFlow.observe(viewLifecycleOwner, Observer { dataFlow ->
            job?.cancel()
            job = lifecycleScope.launch {
                dataFlow.collectLatest { data ->
                    controller.submitData(data)
                }
            }
        })
    }

    companion object {
        private const val LIST_ITEM_PREFIX = "nearby_user"

        internal fun newInstance() = NearbyUserListFragment()
    }
}
