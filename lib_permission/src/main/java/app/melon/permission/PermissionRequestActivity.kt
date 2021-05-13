package app.melon.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.melon.permission.databinding.ActivityPermissionRequestBinding


internal class PermissionRequestActivity : AppCompatActivity() {

    private var _binding: ActivityPermissionRequestBinding? = null
    private val binding get() = _binding!!

    private val action get() = requireNotNull(intent.getSerializableExtra(KEY_ACTION) as PermissionActions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPermissionRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideSystemUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hideSystemUI() {
        val color = ContextCompat.getColor(this, R.color.colorPrimary)
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun initView() {
        binding.back.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        binding.negative.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        binding.positive.setOnClickListener {
            if (action is PermissionDenial) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.positive.setText(when(action) {
            is PermissionRequest -> R.string.permission_continue
            is PermissionDenial -> R.string.permission_view_app_info
        })
        binding.negative.setText(when(action) {
            is PermissionRequest -> R.string.permission_go_back
            is PermissionDenial -> R.string.permission_not_now
        })

        action.title.takeIf { it != -1 }?.let { binding.title.setText(it) }
        action.subTitle.takeIf { it != -1 }?.let {
            when (action) {
                is PermissionRequest -> binding.subtitle.setText(it)
                is PermissionDenial -> {
                    binding.subtitle.text = (action as PermissionDenial).permissions.joinToString(
                        separator = ",\n",
                        postfix = "\n${getString(it)}"
                    )
                }
            }
        }
    }

    companion object {
        private const val KEY_ACTION = "KEY_ACTION"

        internal fun prepareIntent(context: Context, action: PermissionActions): Intent {
            return Intent(context, PermissionRequestActivity::class.java).apply {
                putExtra(KEY_ACTION, action)
            }
        }
    }
}