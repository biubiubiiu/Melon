package app.melon.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import app.melon.base.ui.databinding.ActivityCommonFragmentWithToolbarBinding
import app.melon.user.api.FollowingUserList
import app.melon.user.api.IUserService
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getColorCompat
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class FollowingActivity: DaggerAppCompatActivity() {

    private val binding: ActivityCommonFragmentWithToolbarBinding by viewBinding()

    private val uid get() = intent.getStringExtra(KEY_USER_ID)!!

    @Inject internal lateinit var userService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.fragment_container,
                    userService.buildUserListFragment(
                        FollowingUserList(
                            listItemIdPrefix = "nearby_user",
                            uid = uid
                        )
                    )
                )
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(R.string.app_common_following)
        binding.toolbar.setNavigationIconTint(getColorCompat(R.color.colorPrimary))
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"

        internal fun start(context: Context, id: String) {
            val intent = Intent(context, FollowingActivity::class.java).apply {
                putExtra(KEY_USER_ID, id)
            }
            context.startActivity(intent)
        }
    }
}