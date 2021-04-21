package app.melon.locationpermission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import app.melon.databinding.ActivityLocationRequestBinding
import app.melon.home.MainActivity
import app.melon.location.LocationHelper
import app.melon.util.delegates.viewBinding


class LocationPermissionActivity : AppCompatActivity() {

    private val binding: ActivityLocationRequestBinding by viewBinding()

    private var bottomSheet: LocationPermissionBottomSheet? = null

    private val requestLocatingPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
            val isAllGranted = !result.values.contains(element = false)
            if (isAllGranted) {
                navigateToHomepage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.action.setOnClickListener {
            requestLocatePermission()
        }
        binding.moreInfo.setOnClickListener {
            bottomSheet = LocationPermissionBottomSheet()
            bottomSheet?.show(supportFragmentManager, "info")
        }
    }

    override fun onDestroy() {
        bottomSheet?.dismissAllowingStateLoss()
        bottomSheet = null
        super.onDestroy()
    }

    fun requestLocatePermission() {
        requestLocatingPermission.launch(LocationHelper.requiredPermissions)
    }

    private fun navigateToHomepage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        internal fun start(context: Context) {
            context.startActivity(Intent(context, LocationPermissionActivity::class.java))
        }
    }
}