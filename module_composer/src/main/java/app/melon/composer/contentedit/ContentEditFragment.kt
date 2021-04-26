package app.melon.composer.contentedit

import android.os.Bundle
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.melon.base.ui.decoration.SpaceItemDecoration
import app.melon.base.ui.extensions.makeInvisible
import app.melon.base.ui.extensions.makeVisible
import app.melon.base.ui.extensions.navigateUpOrFinish
import app.melon.composer.ComposerViewModel
import app.melon.composer.R
import app.melon.composer.common.MediaStoreImage
import app.melon.composer.databinding.FragmentContentEditBinding
import app.melon.composer.permission.AccessGallery
import app.melon.composer.permission.AcquireLocation
import app.melon.permission.PermissionHelperOwner
import app.melon.util.extensions.afterTextChanged
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getResourceColor
import coil.load
import coil.transform.CircleCropTransformation

internal class ContentEditFragment : Fragment() {

    private var _binding: FragmentContentEditBinding? = null
    private val binding get() = _binding!!

    private val composerViewModel: ComposerViewModel by activityViewModels()

    private val photosAdapter = PhotoAdapter(::removePhoto)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        composerViewModel.images.observe(this, Observer {
            showPhotos(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.onClose = { findNavController().navigateUpOrFinish(requireActivity()) }
        binding.toolbar.onTrailingAreaClicked = {
            // TODO
        }
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

        composerViewModel.textInputProgress.observe(viewLifecycleOwner, Observer { progress ->
            binding.progress.setProgressCompat(progress, true)
        })

        composerViewModel.avatarUrl.observe(viewLifecycleOwner, Observer { url ->
            binding.avatar.load(url) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}