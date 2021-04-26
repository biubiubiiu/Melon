package app.melon.base.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding


class MelonButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

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

    fun update(style: Style) {
        update(style, this.text.toString())
    }

    fun update(style: Style, @StringRes resId: Int) {
        update(style, context.getString(resId))
    }

    fun update(style: Style, text: String) {
        this.text = text
        setBackgroundResource(
            when (style) {
                Style.SOLID -> R.drawable.round_rect
                Style.BORDER -> R.drawable.round_rect_border
            }
        )
        setTextColor(
            when (style) {
                Style.SOLID -> ContextCompat.getColor(context, R.color.white)
                Style.BORDER -> ContextCompat.getColor(context, R.color.colorPrimary)
            }
        )
    }

    enum class Style {
        SOLID, BORDER
    }

    private val Int.dpInt get() = (context.resources.displayMetrics.density * this + 0.5f).toInt()
}
