package app.melon.user.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.updatePadding
import app.melon.user.R
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getColorCompat


class FollowUserBtn @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var currentState: FollowState? = null

    init {
        updatePadding(
            left = 16.dpInt,
            right = 16.dpInt,
            top = 8.dpInt,
            bottom = 8.dpInt
        )
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        setTypeface(this.typeface, Typeface.BOLD)
    }

    fun setState(following: Boolean) {
        currentState = if (following) FollowState.FOLLOWING else FollowState.UNFOLLOWED
        updateView()
    }

    private fun updateView() {
        val state = currentState ?: return
        when (state) {
            FollowState.FOLLOWING -> {
                setText(R.string.app_common_following)
                setTextColor(context.getColorCompat(R.color.white))
                setBackgroundResource(R.drawable.profile_following_round_rect_solid)
            }
            FollowState.UNFOLLOWED -> {
                setText(R.string.app_common_follow)
                setTextColor(context.getColorCompat(R.color.colorPrimary))
                setBackgroundResource(R.drawable.profile_follow_round_rect_border)
            }
        }
    }

    private enum class FollowState {
        FOLLOWING, UNFOLLOWED
    }
}