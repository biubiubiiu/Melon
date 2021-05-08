package app.melon.user.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.melon.base.framework.BaseFragment
import app.melon.user.R
import app.melon.user.databinding.FragmentUserPostsBinding
import app.melon.util.extensions.ifNotEmpty
import app.melon.util.extensions.viewModelProviderFactoryOf
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class UserPostsFragment : BaseFragment<FragmentUserPostsBinding>() {

    private val transitionName by lazy { requireArguments().getString(KEY_TRANSITION_NAME, "") }

    @Inject internal lateinit var viewModelFactory: UserPostsViewModel.Factory
    @Inject internal lateinit var controllerFactory: UserPostsController.Factory

    private val viewModel: UserPostsViewModel by viewModels {
        viewModelProviderFactoryOf {
            val args = requireArguments()
            viewModelFactory.create(
                id = args.getString(KEY_USER_ID) ?: throw IllegalArgumentException("Missing user id parameter")
            )
        }
    }

    private val controller: UserPostsController by lazy {
        controllerFactory.create(requireContext())
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentUserPostsBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.pagingData.collectLatest {
                controller.submitData(it)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(binding: FragmentUserPostsBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        transitionName.ifNotEmpty { ViewCompat.setTransitionName(binding.root, it) }

        binding.recyclerView.setController(controller)
        setupToolbar()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_more).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_TRANSITION_NAME = "KEY_TRANSITION_NAME"

        fun newInstance(
            uid: String,
            transitionName: String
        ): UserPostsFragment =
            UserPostsFragment().apply {
                arguments = bundleOf(
                    KEY_USER_ID to uid,
                    KEY_TRANSITION_NAME to transitionName
                )
            }
    }
}