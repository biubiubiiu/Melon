package app.melon.user.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.melon.base.framework.BaseFragment
import app.melon.user.databinding.FragmentUserProfileBinding
import app.melon.util.extensions.viewModelProviderFactoryOf
import app.melon.util.formatter.MelonNumberFormatter
import javax.inject.Inject


internal class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() {

    @Inject internal lateinit var viewModelFactory: UserProfileViewModel.Factory
    @Inject internal lateinit var controllerFactory: UserProfileController.Factory

    @Inject internal lateinit var numberFormatter: MelonNumberFormatter

    private val viewModel: UserProfileViewModel by viewModels(::requireActivity) {
        viewModelProviderFactoryOf {
            val args = requireArguments()
            viewModelFactory.create(
                id = args.getString(KEY_USER_ID) ?: throw IllegalArgumentException("Missing user id parameter")
            )
        }
    }

    private val controller by lazy(LazyThreadSafetyMode.NONE) {
        controllerFactory.create(
            context = requireContext(),
            action = object : UserProfileController.Action {
                override fun onShowMoreButtonClick(view: View) {
                    viewModel.viewMorePosts(view)
                }
            }
        )
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentUserProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentUserProfileBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)

        binding.userProfileContent.userProfileList.setController(controller)

        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            binding.userProfileRefreshView.root.isVisible = state.refreshing
            controller.setData(state.user, state.pageItems, state.refreshing)
        })
    }

    companion object {
        private const val KEY_USER_ID = "arg_user_id"
        internal fun newInstance(uid: String): UserProfileFragment =
            UserProfileFragment().apply {
                arguments = bundleOf(KEY_USER_ID to uid)
            }
    }
}