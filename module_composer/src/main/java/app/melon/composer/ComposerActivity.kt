package app.melon.composer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import app.melon.composer.api.ComposerManager
import app.melon.composer.api.ComposerOption
import app.melon.composer.api.ComposerResult
import app.melon.composer.api.ContentCreation
import app.melon.composer.common.ComposerActionsExecutor
import app.melon.composer.databinding.ActivityComposerBinding
import app.melon.composer.gallerygrid.TakePicture
import app.melon.composer.gallerygrid.TakePictureHandler
import app.melon.composer.permission.AccessGallery
import app.melon.composer.permission.AcquireLocation
import app.melon.composer.permission.WriteExternal
import app.melon.permission.PermissionHelper
import app.melon.permission.PermissionHelperOwner
import app.melon.permission.PermissionRequest


class ComposerActivity : AppCompatActivity(), PermissionHelperOwner, ComposerActionsExecutor {

    private var _binding: ActivityComposerBinding? = null
    private val binding get() = _binding!!

    private val composerOption
        get() = intent.getSerializableExtra(ComposerManager.EXTRA_COMPOSER_OPTION) as ComposerOption

    private val accessGalleryPermissionHelper = PermissionHelper(this, AccessGallery)
    private val useCameraPermissionHelper = PermissionHelper(this, WriteExternal)
    private val acquireLocationPermissionHelper = PermissionHelper(this, AcquireLocation)

    private lateinit var actionTakePicture: TakePicture

    private val viewModel: ComposerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityComposerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionTakePicture = TakePictureHandler(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        displayAvatar()

        viewModel.actionSubmit.observe(this, Observer {
            val composerResult = ComposerResult(
                textContent = viewModel.textContent.value ?: "",
                images = viewModel.images.value?.map { it.contentUri } ?: emptyList(),
                location = viewModel.locationInfo.value
            )
            val result = Intent().apply {
                putExtra(ComposerManager.EXTRA_COMPOSER_RESULT, composerResult)
            }
            setResult(Activity.RESULT_OK, result)
            finish()
        })

        viewModel.actionLeave.observe(this, Observer {
            setResult(Activity.RESULT_CANCELED)
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return binding.navHostFragment.findNavController().navigateUp()
    }

    override fun checkPermission(request: PermissionRequest, onPermissionsGranted: () -> Unit) {
        when (request) {
            AccessGallery -> accessGalleryPermissionHelper.checkPermissions(
                onPermissionAllGranted = onPermissionsGranted
            )
            WriteExternal -> useCameraPermissionHelper.checkPermissions(
                onPermissionAllGranted = onPermissionsGranted
            )
            AcquireLocation -> acquireLocationPermissionHelper.checkPermissions(
                onPermissionAllGranted = onPermissionsGranted
            )
        }
    }

    override fun takePicture(uri: Uri, callback: (Boolean) -> Unit) {
        actionTakePicture.takePicture(uri, callback)
    }

    private fun displayAvatar() {
        when (val option = composerOption) {
            is ContentCreation -> viewModel.updateAvatarUrl(option.accountAvatarUrl)
        }
    }
}