package app.melon.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import app.melon.data.entities.Feed
import app.melon.feed.ui.FeedDetailFragment


class FeedDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)
        setupToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, FeedDetailFragment.newInstance(Feed(feedId = "test")))
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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

        fun prepareIntent(context: Context, feed: Feed): Intent {
            return Intent(context, FeedDetailActivity::class.java).apply {
                putExtra(KEY_CACHE_FEED, feed)
            }
        }
    }
}