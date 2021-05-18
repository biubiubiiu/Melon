package app.melon.composer.contentedit

import android.os.Bundle
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.melon.base.framework.BaseFragment
import app.melon.base.ui.decoration.SpaceItemDecoration
import app.melon.base.ui.extensions.makeInvisible
import app.melon.base.ui.extensions.makeVisible
import app.melon.base.ui.insetanimation.RootViewDeferringInsetsCallback
import app.melon.base.ui.insetanimation.TranslateDeferringInsetsAnimationCallback
import app.melon.composer.ComposerViewModel
import app.melon.composer.R
import app.melon.composer.api.AnonymousPost
import app.melon.composer.api.ComposerOption
import app.melon.composer.api.ContentCreation
import app.melon.composer.common.MediaStoreImage
import app.melon.composer.databinding.FragmentContentEditBinding
import app.melon.composer.permission.AccessGallery
import app.melon.composer.permission.AcquireLocation
import app.melon.permission.PermissionHelperOwner
import app.melon.util.extensions.afterTextChanged
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getResourceColor
import coil.load
import coil.loadAny
import coil.transform.CircleCropTransformation


internal class ContentEditFragment : BaseFragment<FragmentContentEditBinding>() {

    private val composerViewModel: ComposerViewModel by activityViewModels()

    private val photosAdapter = PhotoAdapter(::removePhoto)

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentContentEditBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        composerViewModel.images.observe(this, Observer {
            showPhotos(it)
        })
    }

    override fun onViewCreated(binding: FragmentContentEditBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        ViewCompat.setWindowInsetsAnimationCallback(binding.root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root, deferringInsetsListener)
        ViewCompat.setWindowInsetsAnimationCallback(
            binding.entriesContainer,
            TranslateDeferringInsetsAnimationCallback(
                view = binding.entriesContainer,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
            )
        )

        binding.toolbar.onClose = { composerViewModel.leave() }
        binding.toolbar.onTrailingAreaClicked = { composerViewModel.submitAndLeave() }
        binding.toolbar.trailingButtonText = getString(R.string.composer_post_content)

        binding.photos.also { v ->
            v.layoutManager = LinearLayoutManager(v.context, RecyclerView.HORIZONTAL, false)
            v.addItemDecoration(SpaceItemDecoration(8.dpInt, SpaceItemDecoration.RIGHT))
            v.adapter = photosAdapter

            ItemTouchHelper(SwapCallback(photosAdapter) { from, to ->
                composerViewModel.swapImagePosition(from, to)
            }).attachToRecyclerView(v)
        }

        binding.galleryEntry.setOnClickListener {
            (activity as? PermissionHelperOwner)?.checkPermission(AccessGallery) {
                findNavController().navigate(R.id.action_content_fragment_to_gallery_grid)
            }
        }
        binding.poiEntry.setOnClickListener {
            (activity as? PermissionHelperOwner)?.checkPermission(AcquireLocation) {
                findNavController().navigate(R.id.action_content_fragment_to_select_location)
            }
        }

        binding.textInput.afterTextChanged {
            composerViewModel.contentChanged(it)
        }
        binding.textInput.doAfterTextChanged {
            if (it == null || it.length < ComposerViewModel.MAX_INPUT_LENGTH) {
                return@doAfterTextChanged
            }
            it.getSpans(0, it.length, Object::class.java).forEach { span ->
                it.removeSpan(span)
            }
            it.setSpan(
                BackgroundColorSpan(getResourceColor(R.color.cerise_red)),
                ComposerViewModel.MAX_INPUT_LENGTH,
                it.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        composerViewModel.launchOption.observe(viewLifecycleOwner, Observer { option ->
            updateAvatar(option)
            updateInputHint(option)
        })

        composerViewModel.textInputProgress.observe(viewLifecycleOwner, Observer { progress ->
            binding.progress.setProgressCompat(progress, true)
        })

        composerViewModel.inputValid.observe(viewLifecycleOwner, Observer { isValid ->
            binding.toolbar.enableButton = isValid
        })

        composerViewModel.locationInfo.observe(viewLifecycleOwner, Observer { info ->
            if (info != null) {
                binding.location.makeVisible()
                binding.location.text = info.poiName
            } else {
                binding.location.makeInvisible()
            }
        })
    }

    private fun showPhotos(images: List<MediaStoreImage>) {
        if (images.size == 1) {
            binding.photos.makeInvisible()
            binding.singlePhotoContainer.isVisible = true

            val image = images[0]
            binding.photo.load(image.contentUri)
            binding.remove.setOnClickListener {
                removePhoto(image)
            }
        } else {
            binding.photos.makeVisible()
            binding.singlePhotoContainer.isVisible = false
        }
        photosAdapter.submitList(images)
    }

    private fun removePhoto(image: MediaStoreImage) {
        composerViewModel.deselectImage(image)
    }

    private fun updateAvatar(option: ComposerOption) {
        val avatar: Any? = when (option) {
            is ContentCreation -> option.accountAvatarUrl
            is AnonymousPost -> R.drawable.ic_avatar_anonymous
        }
        binding.avatar.loadAny(avatar) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun updateInputHint(option: ComposerOption) {
        val hint = when (option) {
            is ContentCreation -> R.string.composer_input_hint
            is AnonymousPost -> R.string.composer_input_hint_2
        }
        binding.textInput.setHint(hint)
    }
}