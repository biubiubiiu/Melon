package app.melon.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import app.melon.user.ui.UserProfileContainerFragment
import app.melon.util.event.OnBackPressedHandler
import dagger.android.support.DaggerAppCompatActivity


class UserProfileActivity : DaggerAppCompatActivity() {

    private val uid: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra(KEY_USER_ID) ?: "" }

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(USER_PROFILE_FRAGMENT_CONTAINER_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if (savedInstanceState == null) {
            val fragment = UserProfileContainerFragment.newInstance(uid)
            supportFragmentManager.commit {
                add(R.id.container, fragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_user_profile, menu)
        return true
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
        private val USER_PROFILE_FRAGMENT_CONTAINER_ID = R.id.container
        private const val KEY_USER_ID = "KEY_USER_ID"

        fun start(context: Context, uid: String) {
            val intent = Intent(context, UserProfileActivity::class.java).apply {
                putExtra(KEY_USER_ID, uid)
            }
            context.startActivity(intent)
        }
    }
}