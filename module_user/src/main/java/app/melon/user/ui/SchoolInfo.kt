package app.melon.user.ui

import android.content.Context
import android.widget.TextView
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.User
import app.melon.user.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class SchoolInfo : EpoxyModelWithHolder<SchoolInfo.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_school_info

    @EpoxyAttribute lateinit var context: Context
    @EpoxyAttribute lateinit var user: User

    private val defaultText by lazy { context.getString(R.string.app_common_not_set) }

    override fun bind(holder: Holder) {
        with(holder) {
            hometownView.text = user.hometown.orEmpty().ifBlank { defaultText }
            schoolView.text = user.school.orEmpty().ifBlank { defaultText }
            collegeView.text = user.college.orEmpty().ifBlank { defaultText }
            majorView.text = user.major.orEmpty().ifBlank { defaultText }
            degreeView.text = user.degree.orEmpty().ifBlank { defaultText }
        }
    }

    class Holder : BaseEpoxyHolder() {
        val hometownView by bind<TextView>(R.id.user_profile_hometown)
        val schoolView by bind<TextView>(R.id.user_profile_school)
        val collegeView by bind<TextView>(R.id.user_profile_college)
        val majorView by bind<TextView>(R.id.user_profile_major)
        val degreeView by bind<TextView>(R.id.user_profile_degree)
    }
}