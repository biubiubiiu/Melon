package app.melon.base.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.toColorInt
import androidx.core.view.updatePadding
import app.melon.data.entities.User


class TagView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    companion object {
        private const val PADDING_HORIZONTAL_PX = 16
        private const val PADDING_VERTICAL_PX = 2

        private val TEXT_COLOR = "#FFFFFF".toColorInt()

        private val BG_COLOR_MALE = "#97DBFD".toColorInt()
        private val BG_COLOR_FEMALE = "#FDB3C4".toColorInt()
        private val BG_COLOR_OTHER = "#000000".toColorInt()

        private val BG_DISTANCE = "#888888".toColorInt()

        private const val PREFIX_MALE = "♂"
        private const val PREFIX_FEMALE = "♀"
        private const val PREFIX_HYBRID = "×"
        private const val PREFIX_TRANSGENDER = "⚦"
        private const val PREFIX_GENDERLESS = "⚪"

        private const val PREFIX_DISTANCE = "📍"
    }

    sealed class TagStyle {
        data class Info(
            val showGender: Boolean = true,
            val showAge: Boolean = true,
            val showZodiacSign: Boolean = true
        ) : TagStyle()

        data class Distance(
            val distance: Float
        ) : TagStyle()
    }

    init {
        setTextColor(TEXT_COLOR)
        includeFontPadding = false
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        updatePadding(
            left = PADDING_HORIZONTAL_PX,
            right = PADDING_HORIZONTAL_PX,
            top = PADDING_VERTICAL_PX,
            bottom = PADDING_VERTICAL_PX
        )
        gravity = Gravity.CENTER
    }

    fun bind(
        user: User,
        style: TagStyle
    ) {
        when (style) {
            is TagStyle.Info -> renderInfoStyle(user, style.showGender, style.showAge)
            is TagStyle.Distance -> renderDistanceStyle(user, style.distance)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderInfoStyle(user: User, showGender: Boolean, showAge: Boolean) {
        val prefix = when {
            !showGender -> ""
            user.isMale -> PREFIX_MALE
            user.isFemale -> PREFIX_FEMALE
            user.isHybrid -> PREFIX_HYBRID
            user.isTransgender -> PREFIX_TRANSGENDER
            user.isGenderless -> PREFIX_GENDERLESS
            else -> ""
        }
        val age = when {
            !showAge -> ""
            else -> user.age.toString()
        }
        text = "$prefix$age"
        background = GradientDrawable().apply {
            setColor(
                when {
                    user.isMale -> BG_COLOR_MALE
                    user.isFemale -> BG_COLOR_FEMALE
                    else -> BG_COLOR_OTHER
                }
            )
            cornerRadius = 30f
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderDistanceStyle(user: User, distance: Float) {
        val prefix = PREFIX_DISTANCE
        text = "$prefix ${processDistance(distance)}"
        background = GradientDrawable().apply {
            setColor(BG_DISTANCE)
            cornerRadius = 30f
        }
    }

    private fun processDistance(distance: Float): String {
        return "${distance}km"
    }
}