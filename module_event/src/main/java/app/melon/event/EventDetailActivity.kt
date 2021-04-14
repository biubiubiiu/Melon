package app.melon.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.databinding.ActivityEventDetailBinding
import app.melon.event.detail.EventDetailController
import app.melon.event.detail.EventDetailViewModel
import app.melon.event.detail.EventDetailViewState
import app.melon.event.detail.create
import app.melon.util.delegates.viewBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class EventDetailActivity : DaggerAppCompatActivity() {

    private val id get() = intent.getStringExtra(KEY_EVENT_ID)!!
    private val cache get() = intent.getSerializableExtra(KEY_EVENT_CACHE) as? EventAndOrganiser

    private val binding: ActivityEventDetailBinding by viewBinding()

    @Inject internal lateinit var controllerFactory: EventDetailController.Factory
    private lateinit var controller: EventDetailController

    @Inject internal lateinit var viewModelFactory: EventDetailViewModel.Factory
    private lateinit var viewModel: EventDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjection()
        setupToolbar()
        setupRecyclerView()
        setupButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initInjection() {
        viewModel = viewModelFactory.create(id, cache)
        controller = controllerFactory.create(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.setController(controller)
        viewModel.liveData.observe(this, Observer {
            controller.setData(it.pageItem, it.refreshing)
        })
    }

    private fun setupButton() {
        viewModel.selectObserve(EventDetailViewState::pageItem).observe(this, Observer {
            val event = it?.event ?: return@Observer
            binding.status.setText(
                when {
                    event.isIdle -> R.string.event_idle
                    event.isJoining -> R.string.event_joining
                    event.isExclude -> R.string.event_excluded
                    event.isExpired -> R.string.event_expired
                    else -> return@Observer
                }
            )
        })
        viewModel.selectObserve(EventDetailViewState::changingEventState).observe(this, Observer { updating ->
            binding.status.isEnabled = !updating
            if (updating) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
        })
        binding.status.setOnClickListener {
            viewModel.joinOrLeaveEvent()
        }
    }

    companion object {
        private const val KEY_EVENT_ID = "KEY_EVENT_ID"
        private const val KEY_EVENT_CACHE = "KEY_EVENT_CACHE"

        internal fun start(context: Context, id: String, cache: EventAndOrganiser? = null) {
            val intent = Intent(context, EventDetailActivity::class.java).apply {
                putExtra(KEY_EVENT_ID, id)
                putExtra(KEY_EVENT_CACHE, cache)
            }
            context.startActivity(intent)
        }
    }
}