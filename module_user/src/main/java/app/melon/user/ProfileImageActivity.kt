package app.melon.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import app.melon.permission.helper.EditHelper
import app.melon.user.databinding.ActivityProfileImageBinding
import app.melon.user.ui.image.ProfileImageViewModel
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.showToast
import app.melon.util.storage.StorageHandler
import coil.load
import coil.size.OriginalSize
import coil.size.Precision
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class ProfileImageActivity : DaggerAppCompatActivity() {

    private val binding: ActivityProfileImageBinding by viewBinding()

    private val url: String get() = requireNotNull(intent.getStringExtra(KEY_URL))
    private val isMyProfile: Boolean get() = requireNotNull(intent.getBooleanExtra(KEY_MY_PROFILE, false))

    private val editHelper = EditHelper(
        this,
        onReceiveTakePictureResult = { binding.edit.text = it.toString() },
        onReceiveUriFromAlbum = { onImageSelected(it) }
    )

    @Inject internal lateinit var viewModel: ProfileImageViewModel

    @Inject internal lateinit var storageHandler: StorageHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        viewModel.updateAvatarUrl(url)
        observeImage()
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

    private fun observeImage() {
        viewModel.avatarUrl.observe(this, Observer {
            binding.image.load(it) {
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
        })
    }

    private fun setupEditButton() {
        binding.edit.isVisible = isMyProfile
        binding.edit.setOnClickListener { editHelper.showEditOptions() }
    }

    private fun observeResult() {
        viewModel.updateResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    applicationContext.showToast(R.string.update_profile_success)
                    viewModel.updateAvatarUrl(it.get()) // TODO update local database and finish activity
                }
                is ErrorResult -> showToast(R.string.update_profile_fail)
            }
        })
    }

    private fun onImageSelected(uri: Uri?) {
        binding.edit.text = uri.toString()
        uri ?: return
        val avatar = storageHandler.copyFileToCacheDir(uri, filename = AVATAR_CACHE_FILENAME)
        viewModel.updateAvatar(avatar)
    }

    companion object {
        private const val KEY_URL = "KEY_URL"
        private const val KEY_MY_PROFILE = "KEY_MY_PROFILE"

        private const val AVATAR_CACHE_FILENAME = "updated_avatar"

        // TODO should parse an [uid] so we can enter this page before avatar was loaded
        internal fun start(context: Context, url: String, isMyProfile: Boolean = false) {
            val intent = Intent(context, ProfileImageActivity::class.java).apply {
                putExtra(KEY_URL, url)
                putExtra(KEY_MY_PROFILE, isMyProfile)
            }
            context.startActivity(intent)
        }
    }
}