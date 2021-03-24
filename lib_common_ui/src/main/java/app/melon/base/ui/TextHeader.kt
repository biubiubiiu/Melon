package app.melon.base.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TextHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    @ModelProp(options = [ModelProp.Option.GenerateStringOverloads])
    fun content(content: CharSequence) {
        text = content
    }

    @JvmOverloads
    @ModelProp
    fun color(@ColorInt color: Int = Color.WHITE) {
        setTextColor(color)
    }

    @JvmOverloads
    @ModelProp
    fun background(@DrawableRes bgResource: Int = R.color.bgPrimary) {
        setBackgroundResource(bgResource)
    }

    @JvmOverloads
    @CallbackProp
    fun onClickListener(listener: ((View) -> Unit)? = null) {
        setOnClickListener { listener?.invoke(this) }
    }

    @JvmOverloads
    @ModelProp
    fun transitionName(transitionName: String = "") {
        if (transitionName.isNotEmpty()) {
            ViewCompat.setTransitionName(this, transitionName)
        }
    }

    @ModelProp
    fun textSize(@Px size: Float) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    @ModelProp
    fun padding(paddings: IntArray) {
        when (paddings.size) {
            1 -> setPadding(paddings[0], paddings[0], paddings[0], paddings[0])
            2 -> setPadding(paddings[0], paddings[1], paddings[0], paddings[1])
            4 -> setPadding(paddings[0], paddings[1], paddings[2], paddings[3])
            else -> throw IllegalArgumentException("paddings should be of size 1, 2 or 4")
        }
    }
}
