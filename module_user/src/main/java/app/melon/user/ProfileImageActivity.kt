package app.melon.user

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.melon.permission.PermissionDenial
import app.melon.permission.PermissionRequestActivity
import app.melon.permission.PermissionList
import app.melon.permission.ReadStorage
import app.melon.permission.UseCamera
import app.melon.user.databinding.ActivityProfileImageBinding
import app.melon.user.ui.image.EditOptionsDialogFragment
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.hasPermission
import app.melon.util.extensions.hasPermissions
import app.melon.util.extensions.showToast
import coil.load
import coil.size.OriginalSize
import coil.size.Precision
import java.text.SimpleDateFormat
import java.util.*

class ProfileImageActivity : AppCompatActivity(R.layout.activity_profile_image), EditOptionsDialogFragment.Listener {

    private var _binding: ActivityProfileImageBinding? = null
    private val binding get() = _binding!!

    private val url: String get() = requireNotNull(intent.getStringExtra(KEY_URL))
    private val isMyProfile: Boolean get() = requireNotNull(intent.getBooleanExtra(KEY_MY_PROFILE, false))

    private var pendingProcessCameraImageUri: Uri? = null
    private val takeImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean ->
        binding.edit.text = result.toString()
        if (!result) {
            val resolver = applicationContext.contentResolver
            val url = pendingProcessCameraImageUri ?: return@registerForActivityResult
            resolver.delete(url, null)
        }
    }

    private val selectImageFromAlbum = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        binding.edit.text = uri.toString()
    }

    private val showReadStorageRational = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                requestReadStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            Activity.RESULT_CANCELED -> {
                // No-op
            }
        }
    }

    private val showUseCameraRational = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                requestCameraPermission.launch(PermissionList.permissionsForCamera)
            }
            Activity.RESULT_CANCELED -> {
                // No-op
            }
        }
    }

    private val requestReadStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openAlbum()
            } else {
                val intent = PermissionRequestActivity.prepareIntent(
                    this,
                    PermissionDenial(listOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                )
                startActivity(intent)
            }
        }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
            val isAllGranted = !result.values.contains(element = false)
            if (isAllGranted) {
                openCamera()
            } else {
                val intent = PermissionRequestActivity.prepareIntent(
                    this,
                    PermissionDenial(result.filterValues { !it }.keys.toList())
                )
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadImage()
        setupEditButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun loadImage() {
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
                    throwable.localizedMessage?.let { showToast(it) }
                }
            )
        }
    }

    private fun setupEditButton() {
        binding.edit.isVisible = isMyProfile
        binding.edit.setOnClickListener { showEditOptions() }
    }

    private fun showEditOptions() {
        val fragment = EditOptionsDialogFragment().also { it.setListener(this) }
        fragment.show(supportFragmentManager, "options")
    }

    override fun onSelectOptionCamera() {
        openCamera()
    }

    override fun onSelectOptionAlbum() {
        openAlbum()
    }

    private fun openCamera() {
        if (hasPermissions(*PermissionList.permissionsForCamera)) {
            val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
            val filename = "Melon_${currentDateTime}.jpg"
            val resolver = applicationContext.contentResolver
            val imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            val newSongDetails = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            }
            pendingProcessCameraImageUri = resolver.insert(imageCollection, newSongDetails)
            takeImageFromCamera.launch(pendingProcessCameraImageUri)
        } else {
            val intent = PermissionRequestActivity.prepareIntent(this, UseCamera)
            showUseCameraRational.launch(intent)
        }
    }

    private fun openAlbum() {
        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            selectImageFromAlbum.launch("image/*")
        } else {
            val intent = PermissionRequestActivity.prepareIntent(this, ReadStorage)
            showReadStorageRational.launch(intent)
        }
    }

    companion object {
        private const val KEY_URL = "KEY_URL"
        private const val KEY_MY_PROFILE = "KEY_MY_PROFILE"

        // TODO should parse an [uid] so we can enter this page before avatar was loaded
        fun start(context: Context, url: String, isMyProfile: Boolean = false) {
            val intent = Intent(context, ProfileImageActivity::class.java).apply {
                putExtra(KEY_URL, url)
                putExtra(KEY_MY_PROFILE, isMyProfile)
            }
            context.startActivity(intent)
        }
    }
}