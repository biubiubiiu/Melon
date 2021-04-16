package app.melon.user.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import app.melon.base.databinding.FragmentEpoxyListBinding
import app.melon.base.framework.BasePagingListFragment
import app.melon.data.constants.UserPageType
import app.melon.user.UserPageConfig
import app.melon.user.ui.controller.UserPageController
import app.melon.util.extensions.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class CommonUserFragment : BasePagingListFragment() {

    private val pageIndex get() = arguments?.getInt(KEY_PAGE, -1) ?: -1
    private val pageType get() = arguments?.getInt(KEY_USER_PAGE_TYPE, -1) ?: -1
    private val itemIdPrefix get() = arguments?.getString(KEY_ID_PREFIX) ?: ""
    private val showFollowButton get() = arguments?.getBoolean(KEY_SHOW_FOLLOW_BUTTON, false) ?: false

    private val extraParam get() = arguments?.getBundle(KEY_EXTRA_PARAM)

    @Inject internal lateinit var controllerFactory: UserPageController.Factory
    override val controller by lazy {
        controllerFactory.create(
            context = requireContext(),
            idProvider = { _, position -> "${itemIdPrefix}_$position" },
            showFollowButton = showFollowButton
        )
    }

    @Inject internal lateinit var viewModel: UserListViewModel

    private var fetchJob: Job? = null

    private fun refresh() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.refresh(pageType, extraParam).collectLatest {
                controller.submitData(it)
            }
        }
    }

    override fun onViewCreated(binding: FragmentEpoxyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.recyclerView.run {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        if (isArgumentsValid()) {
            refresh()
        } else {
            context?.showToast("Invalid Arguments with pageIndex = [$pageIndex], pageType = [$pageType], prefix = [$itemIdPrefix]")
        }
    }

    private fun isArgumentsValid(): Boolean {
        return pageType != -1
                && itemIdPrefix.isNotEmpty()
    }

    override fun invalidate() {
        // No-op
    }

    companion object {
        private const val KEY_PAGE = "KEY_PAGE"
        private const val KEY_USER_PAGE_TYPE = "KEY_USER_PAGE_TYPE"
        private const val KEY_ID_PREFIX = "KEY_ID_PREFIX"
        private const val KEY_SHOW_FOLLOW_BUTTON = "KEY_SHOW_FOLLOW_BUTTON"
        private const val KEY_EXTRA_PARAM = "KEY_EXTRA_PARAM"

        fun newInstance(
            page: Int,
            config: UserPageConfig,
            extraParams: Bundle? = null
        ) = newInstance(
            page,
            config.pageType,
            config.idPrefix,
            config.showFollowButton,
            extraParams
        )

        fun newInstance(
            page: Int,
            @UserPageType pageType: Int,
            idPrefix: String,
            showFollowButton: Boolean,
            extraParams: Bundle? = null
        ) = CommonUserFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_PAGE, page)
                putInt(KEY_USER_PAGE_TYPE, pageType)
                putString(KEY_ID_PREFIX, idPrefix)
                putBoolean(KEY_SHOW_FOLLOW_BUTTON, showFollowButton)
                putBundle(KEY_EXTRA_PARAM, extraParams)
            }
        }
    }
}