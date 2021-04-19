package app.melon.event.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.TagView
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.R
import app.melon.util.formatter.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class EventItem : EpoxyModelWithHolder<EventItem.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_event

    @EpoxyAttribute lateinit var detailEntryClickListener: (EventAndOrganiser) -> Unit
    @EpoxyAttribute lateinit var profileEntryClickListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: EventAndOrganiser
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter

    @EpoxyAttribute var showCoarseLocationInfo = true

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListener(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.organiser.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.organiser.username
            schoolView.text = item.organiser.school
            userTag.render(TagView.TagStyle.Info(user = item.organiser, showZodiacSign = false))
            distanceView.text = "TODO"
            titleView.text = item.event.title
            contentView.text = item.event.content
            timeRangeView.text =
                "${formatter.formatMediumDateTime(item.event.startTime)} - ${formatter.formatMediumDateTime(item.event.endTime)}"

            locationView.isVisible = showCoarseLocationInfo
            if (showCoarseLocationInfo) {
                locationView.text = item.event.location
            }

            costView.isVisible = item.event.cost != null
            item.event.cost?.let { costView.text = it.toString() }

            demandView.isVisible = item.event.demand != null
            item.event.demand?.let { demandView.text = it }
        }
    }

    private fun setupListener(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { detailEntryClickListener.invoke(item) }
            avatarView.setOnClickListener { profileEntryClickListener.invoke(item.organiser.id) }
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