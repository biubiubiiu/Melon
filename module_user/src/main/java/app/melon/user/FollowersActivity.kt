package app.melon.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import app.melon.base.ui.databinding.ActivityCommonFragmentWithToolbarBinding
import app.melon.data.constants.FOLLOWERS_USER
import app.melon.user.api.UserServiceConstants
import app.melon.user.ui.CommonUserFragment
import app.melon.util.delegates.viewBinding


class FollowersActivity : AppCompatActivity() {

    private val binding: ActivityCommonFragmentWithToolbarBinding by viewBinding()

    private val uid get() = intent.getStringExtra(KEY_USER_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.fragment_container, CommonUserFragment.newInstance(
                        -1,
                        UserPageConfig(
                            pageType = FOLLOWERS_USER,
                            idPrefix = "followers_user"
                        ),
                        extraParams = Bundle().apply {
                            putString(UserServiceConstants.PARAM_USER_ID, uid)
                        }
                    )
                )
            }
        }
    }

    private fun setupToolbar() {
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
            val intent = Intent(context, FollowersActivity::class.java).apply {
                putExtra(KEY_USER_ID, id)
            }
            context.startActivity(intent)
        }
    }
}