package app.melon.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.melon.permission.PermissionHelper
import app.melon.user.databinding.ActivityProfileImageBinding
import app.melon.user.image.ImageViewModelFactory
import app.melon.user.image.ProfileImageViewModel
import app.melon.user.image.TakePicture
import app.melon.user.image.TakePictureHandler
import app.melon.user.permission.ReadStorage
import app.melon.user.permission.WriteExternal
import app.melon.user.ui.edit.EditOptionsDialogFragment
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.showToast
import app.melon.util.savestate.withFactory
import app.melon.util.storage.StorageHandler
import coil.load
import coil.size.OriginalSize
import coil.size.Precision
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch
import javax.inject.Inject


internal class ProfileImageActivity : DaggerAppCompatActivity() {

    private val binding: ActivityProfileImageBinding by viewBinding()

    private val url: String get() = requireNotNull(intent.getStringExtra(KEY_URL))
    private val uid: String get() = requireNotNull(intent.getStringExtra(KEY_USER_ID))

    private val readStoragePermissionHelper = PermissionHelper(this, ReadStorage)
    private val useCameraPermissionHelper = PermissionHelper(this, WriteExternal)

    private val selectImageFromAlbum =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleResult(uri)
        }

    private lateinit var actionTakePicture: TakePicture

    @Inject internal lateinit var viewModelFactory: ImageViewModelFactory
    private val viewModel: ProfileImageViewModel by viewModels {
        withFactory(viewModelFactory)
    }

    @Inject internal lateinit var storageHandler: StorageHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        actionTakePicture = TakePictureHandler(this)
        loadAvatar()
        setupEditButton()
        observeResult()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideSystemUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hideSystemUI() {
        val color = getColorCompat(R.color.profile_image_preview_background)
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun loadAvatar() {
        binding.image.load(url) {
            crossfade(true)
            size(OriginalSize)
            precision(Precision.EXACT)
            listener(
                onStart = { binding.progressBar.isVisible = true },
                onSuccess = { _, _ -> binding.progressBar.isVisible = false },
                onCancel = { binding.progressBar.isVisible = false },
                onError = { _, throwable: Throwable ->
                    binding.progressBar.isVisible = false
                    throwable.localizedMessage?.let { message -> showToast(message) }
                }
            )
        }
    }

    private fun setupEditButton() {
        binding.edit.isVisible = viewModel.checkIsMyProfile(uid)
        binding.edit.setOnClickListener { showEditOptions() }
    }

    private fun showEditOptions() {
        val fragment = EditOptionsDialogFragment()
        fragment.setListener(object : EditOptionsDialogFragment.Listener {
            override fun onSelectOptionCamera() {
                takePicture()
            }

            override fun onSelectOptionAlbum() {
                readFromGallery()
            }
        })
        fragment.show(supportFragmentManager, "options")
    }

    private fun takePicture() {
        useCameraPermissionHelper.checkPermissions(
            onPermissionAllGranted = {
                lifecycleScope.launch {
                    viewModel.createPhotoUri()?.let { uri ->
                        viewModel.saveTemporarilyPhotoUri(uri)
                        actionTakePicture.takePicture(uri) { result ->
                            handleTakePictureResult(result)
                        }
                    }
                }
            }
        )
    }

    private fun readFromGallery() {
        readStoragePermissionHelper.checkPermissions(
            onPermissionAllGranted = {
                selectImageFromAlbum.launch("image/*")
            }
        )
    }

    private fun observeResult() {
        viewModel.updateResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    lifecycleScope.launch {
                        viewModel.syncAvatarUpdateToLocal(
                            uid,
                            it.get()
                        )
                    }
                }
                is ErrorResult -> showToast(R.string.update_profile_fail)
            }
        })
        viewModel.avatarChangeEvent.observe(this, Observer { event ->
            val uri = event.getContentIfNotHandled() ?: return@Observer
            val avatar = storageHandler.copyFileToCacheDir(uri, filename = AVATAR_CACHE_FILENAME)
            viewModel.updateAvatar(avatar)
        })
        viewModel.syncResult.observe(this, Observer {
            applicationContext.showToast(R.string.update_profile_success)
            finish()
        })
    }

    private fun handleTakePictureResult(success: Boolean) {
        if (success) {
            viewModel.temporaryPhotoUri?.let {
                viewModel.avatarChanged(it)
                viewModel.saveTemporarilyPhotoUri(null)
            }
        } else {
            viewModel.deleteTemporaryUri()
        }
    }

    private fun handleResult(uri: Uri?) {
        if (uri != null) {
            viewModel.avatarChanged(uri)
        }
    }

    companion object {
        private const val KEY_URL = "KEY_URL"
        private const val KEY_USER_ID = "KEY_UESR_ID"

        private const val AVATAR_CACHE_FILENAME = "updated_avatar"

        internal fun start(context: Context, url: String, uid: String) {
            val intent = Intent(context, ProfileImageActivity::class.java).apply {
                putExtra(KEY_URL, url)
                putExtra(KEY_USER_ID, uid)
            }
            context.startActivity(intent)
        }
    }
}