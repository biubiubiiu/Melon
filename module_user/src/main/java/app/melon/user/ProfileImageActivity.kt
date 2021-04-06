package app.melon.user

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.melon.user.databinding.ActivityProfileImageBinding
import app.melon.user.edit.EditHelper
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.showToast
import coil.load
import coil.size.OriginalSize
import coil.size.Precision

class ProfileImageActivity : AppCompatActivity(R.layout.activity_profile_image) {

    private var _binding: ActivityProfileImageBinding? = null
    private val binding get() = _binding!!

    private val url: String get() = requireNotNull(intent.getStringExtra(KEY_URL))
    private val isMyProfile: Boolean get() = requireNotNull(intent.getBooleanExtra(KEY_MY_PROFILE, false))

    private val editHelper = EditHelper(
        this,
        onReceiveTakePictureResult = { binding.edit.text = it.toString() },
        onReceiveUriFromAlbum = { binding.edit.text = it.toString() }
    )

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
        binding.edit.setOnClickListener { editHelper.showEditOptions() }
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