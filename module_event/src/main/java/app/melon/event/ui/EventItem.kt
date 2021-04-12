package app.melon.event.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.TagView
import app.melon.data.entities.Event
import app.melon.event.R
import app.melon.util.time.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class EventItem : EpoxyModelWithHolder<EventItem.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_event

    @EpoxyAttribute lateinit var detailEntryClickListener: (Event) -> Unit
    @EpoxyAttribute lateinit var profileEntryClickListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: Event
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter

    @EpoxyAttribute var showCoarseLocationInfo = true

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListener(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            val user = item.organiser!!
            avatarView.load(user.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = user.username
            schoolView.text = user.school
            userTag.bind(user, TagView.TagStyle.Info(showZodiacSign = false))
            distanceView.text = "TODO"
            titleView.text = item.title
            contentView.text = item.content
            timeRangeView.text =
                "${formatter.formatMediumDateTime(item.startTime)} - ${formatter.formatMediumDateTime(item.endTime)}"

            locationView.isVisible = showCoarseLocationInfo
            if (showCoarseLocationInfo) {
                locationView.text = item.location
            }

            costView.isVisible = item.cost != null
            item.cost?.let { costView.text = it.toString() }

            demandView.isVisible = item.demand != null
            item.demand?.let { demandView.text = it }
        }
    }

    private fun setupListener(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { detailEntryClickListener.invoke(item) }
            avatarView.setOnClickListener { profileEntryClickListener.invoke(item.organiser!!.id) }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val containerView: View by bind(R.id.event_container)
        internal val avatarView: ImageView by bind(R.id.avatar)
        internal val usernameView: TextView by bind(R.id.username)
        internal val schoolView: TextView by bind(R.id.user_school)
        internal val userTag: TagView by bind(R.id.user_tag)
        internal val distanceView: TextView by bind(R.id.user_distance)
        internal val titleView: TextView by bind(R.id.title)
        internal val contentView: TextView by bind(R.id.content)
        internal val timeRangeView: TextView by bind(R.id.time_range)
        internal val costView: TextView by bind(R.id.cost)
        internal val locationView: TextView by bind(R.id.location_name)
        internal val demandView: TextView by bind(R.id.demand)
    }
}