package app.melon.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import app.melon.event.databinding.ActivityNearbyEventsBinding
import app.melon.event.nearby.NearbyEventsViewModel
import app.melon.event.nearby.ViewModelFactory
import app.melon.util.delegates.viewBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class NearbyEventsActivity : DaggerAppCompatActivity() {

    private val binding: ActivityNearbyEventsBinding by viewBinding()

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<NearbyEventsViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_view_my_events -> {
                // TODO
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    companion object {
        internal fun start(context: Context) {
            context.startActivity(Intent(context, NearbyEventsActivity::class.java))
        }
    }
}