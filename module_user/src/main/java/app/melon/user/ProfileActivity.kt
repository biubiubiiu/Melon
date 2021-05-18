package app.melon.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import app.melon.base.ui.databinding.CommonFragmentContainerBinding
import app.melon.framework.ComposerEntryActivity
import app.melon.user.ui.UserProfileContainerFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.event.OnBackPressedHandler


internal class ProfileActivity : ComposerEntryActivity() {

    private val binding: CommonFragmentContainerBinding by viewBinding()

    private val uid: String get() = intent.getStringExtra(KEY_USER_ID) ?: ""
    private val isMyProfile get() = uid == "fake_uid" // TODO use UserManager instead

    private val fragmentContainerId get() = binding.fragmentContainer.id
    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(fragmentContainerId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = UserProfileContainerFragment.newInstance(uid)
            supportFragmentManager.commit {
                add(fragmentContainerId, fragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (isMyProfile) {
            super.onCreateOptionsMenu(menu)
        } else {
            menuInflater.inflate(R.menu.menu_user_profile, menu)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (currentFragment?.onOptionsItemSelected(item) == true) {
            return true
        }
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (handleFragmentOnBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    private fun handleFragmentOnBackPressed(): Boolean {
        return (currentFragment as? OnBackPressedHandler)?.onBackPressed() ?: false
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"

        internal fun start(context: Context, uid: String = "fake_uid") {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(KEY_USER_ID, uid)
            }
            context.startActivity(intent)
        }
    }
}