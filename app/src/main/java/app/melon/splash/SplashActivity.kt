package app.melon.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import app.melon.account.api.IAccountService
import app.melon.account.api.UserManager
import app.melon.databinding.ActivitySplashBinding
import app.melon.home.MainActivity
import app.melon.location.LocationHelper
import app.melon.locationpermission.LocationPermissionActivity
import app.melon.util.extensions.hasPermissions
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashActivity : DaggerAppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding: ActivitySplashBinding get() = _binding!!

    @Inject internal lateinit var userManager: UserManager
    @Inject internal lateinit var locationHelper: LocationHelper
    @Inject internal lateinit var accountService: IAccountService

    private val hasLocatePermission get() = hasPermissions(*LocationHelper.requiredPermissions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splash.progress = 0f
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun init() {
        lifecycleScope.launch {
            val loginTask = async { userManager.loginUser() }
            val locateTask = if (hasLocatePermission) async { locationHelper.tryLocate() } else null
            val deferreds = listOfNotNull(loginTask, locateTask)

            val results = deferreds.awaitAll()
            val loginSuccess = results[0] as Boolean
            val locateSuccess = results.getOrNull(1) as? Boolean ?: false

            if (loginSuccess) {
                binding.splash.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        if (locateSuccess) {
                            navigateToHomepage()
                        } else {
                            navigateToPermissionRequestPage()
                        }
                    }
                })
                binding.splash.playAnimation()
            } else {
                accountService.startLogin(this@SplashActivity)
            }
        }
    }

    private fun navigateToHomepage() {
        MainActivity.start(this)
        finish()
    }

    private fun navigateToPermissionRequestPage() {
        LocationPermissionActivity.start(this)
        finish()
    }
}