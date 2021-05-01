package app.melon.composer.gallerygrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import app.melon.composer.ComposerViewModel
import app.melon.composer.R
import app.melon.composer.common.AddMediaViewModel
import app.melon.composer.common.ComposerActionsExecutor
import app.melon.composer.common.GalleryImagesViewModel
import app.melon.composer.common.MediaStoreImage
import app.melon.composer.databinding.FragmentGalleryGridBinding
import app.melon.composer.permission.WriteExternal
import app.melon.permission.PermissionHelperOwner
import kotlinx.coroutines.launch


internal class GalleryGridFragment : Fragment() {

    private var _binding: FragmentGalleryGridBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GalleryGridViewModel by viewModels()
    private val addMediaViewModel: AddMediaViewModel by viewModels()

    private val galleryViewModel: GalleryImagesViewModel by activityViewModels()
    private val composerViewModel: ComposerViewModel by activityViewModels()

    private val galleryAdapter = GalleryItemAdapter(::updateSelection)
    private val cameraAdapter = CameraAdapter(::takePicture)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        galleryViewModel.loadImages()
        viewModel.syncSelection(composerViewModel.images.value ?: emptyList())

        val allImages = galleryViewModel.images
        val selectedImages = viewModel.selections
        val imageWithSelection: MediatorLiveData<List<GalleryGridItem>> = MediatorLiveData()
        imageWithSelection.addSource(allImages) { images ->
            imageWithSelection.value = images.map { image ->
                GalleryGridItem(
                    image,
                    selectedImages.value?.contains(image) == true,
                    selectedImages.value?.let { it.size >= MAX_SELECTED_IMAGE } ?: false
                )
            }
        }
        imageWithSelection.addSource(selectedImages) { selection ->
            val curr = imageWithSelection.value ?: emptyList()
            imageWithSelection.value = curr.map { item ->
                item.copy(
                    isSelected = selection.contains(item.image),
                    reachMaxSelection = selection.size >= MAX_SELECTED_IMAGE
                )
            }
        }
        imageWithSelection.observe(this, Observer { images ->
            galleryAdapter.submitList(images)
        })

        addMediaViewModel.currentMedia.observe(this, Observer {
            viewModel.selectImage(it)
            findNavController().navigateUp()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val insetsListener = OnApplyWindowInsetsListener { v, insets ->
            val type = WindowInsetsCompat.Type.systemBars()
            val typeInsets = insets.getInsets(type)
            v.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root, insetsListener)

        binding.gallery.also { v ->
            v.setHasFixedSize(true)
            v.itemAnimator = null
            v.layoutManager = GridLayoutManager(v.context, 3)
            v.adapter = ConcatAdapter(cameraAdapter, galleryAdapter)
        }

        binding.toolbar.also { v ->
            v.onClose = { findNavController().navigateUp() }
            v.onTrailingAreaClicked = {
                composerViewModel.updateSelection(viewModel.selections.value ?: emptyList())
                findNavController().navigateUp()
            }
        }

        viewModel.selections.observe(viewLifecycleOwner, Observer { images ->
            binding.toolbar.trailingText = when {
                images.isNotEmpty() -> getString(R.string.label_add_photos_with_quantity, images.size)
                images.isEmpty() -> getString(R.string.label_done)
                else -> return@Observer
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun takePicture() {
        (activity as? PermissionHelperOwner)?.checkPermission(WriteExternal) {
            viewLifecycleOwner.lifecycleScope.launch {
                addMediaViewModel.createPhotoUri()?.let { uri ->
                    addMediaViewModel.saveTemporarilyPhotoUri(uri)
                    (activity as? ComposerActionsExecutor)?.takePicture(uri) { result ->
                        handleTakePictureResult(result)
                    }
                }
            }
        }
    }

    private fun updateSelection(image: MediaStoreImage, isSelected: Boolean) {
        if (!isSelected) viewModel.selectImage(image) else viewModel.deselectImage(image)
    }

    private fun handleTakePictureResult(success: Boolean) {
        if (success) {
            addMediaViewModel.temporaryPhotoUri?.let {
                addMediaViewModel.loadCameraMedia(it)
                addMediaViewModel.saveTemporarilyPhotoUri(null)
            }
        } else {
            addMediaViewModel.deleteTemporaryUri()
        }
    }

    private companion object {
        const val MAX_SELECTED_IMAGE = 4
    }
}