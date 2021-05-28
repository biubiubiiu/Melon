package app.melon.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.databinding.ActivityFeedDetailBinding
import app.melon.feed.ui.FeedDetailFragment
import app.melon.framework.ComposerEntryActivity
import app.melon.util.delegates.viewBinding


class FeedDetailActivity : ComposerEntryActivity() {

    private val cache by lazy { requireNotNull(intent.getSerializableExtra(KEY_CACHE_FEED) as FeedAndAuthor) }

    private val binding: ActivityFeedDetailBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, FeedDetailFragment.newInstance(cache))
            }
        }
    }

    private fun setupToolbar() {
        if (true) { // TODO check feed is anonymous
            binding.toolbar.setTitle(R.string.feed_detail_title)
        } else {
            binding.toolbar.setTitle(R.string.anonymous_feed_detail_title)
        }
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
        private const val KEY_CACHE_FEED = "KEY_CACHE_FEED"

        internal fun prepareIntent(context: Context, feed: FeedAndAuthor): Intent {
            return Intent(context, FeedDetailActivity::class.java).apply {
                putExtra(KEY_CACHE_FEED, feed)
            }
        }
    }
}