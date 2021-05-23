package app.melon.im.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import app.melon.im.databinding.ActivityMessageBinding
import app.melon.util.delegates.viewBinding


internal class MessageActivity : AppCompatActivity() {

    private val binding: ActivityMessageBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupFragment() {
        supportFragmentManager.takeIf { it.findFragmentByTag(FRAGMENT_TAG) == null }
            ?.commit {
                add(
                    binding.fragmentContainer.id,
                    MessageListFragment.newInstance(),
                    FRAGMENT_TAG
                )
            }
    }

    companion object {
        private const val FRAGMENT_TAG = "fragment_messages"

        internal fun start(context: Context) {
            val intent = Intent(context, MessageActivity::class.java)
            context.startActivity(intent)
        }
    }
}