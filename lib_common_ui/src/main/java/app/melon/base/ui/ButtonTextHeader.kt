package app.melon.base.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.view.setPadding
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ButtonTextHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val button = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        gravity = Gravity.CENTER_VERTICAL
    }
    private val textView = TextView(context)

    init {
        orientation = HORIZONTAL
        addView(button)
        addView(textView)
    }

    @ModelProp(options = [ModelProp.Option.GenerateStringOverloads])
    fun textContent(content: CharSequence) {
        textView.text = content
    }

    @JvmOverloads
    @ModelProp
    fun textColor(@ColorInt color: Int = Color.WHITE) {
        textView.setTextColor(color)
    }

    @JvmOverloads
    @ModelProp
    fun textTypeface(typeface: Int = Typeface.NORMAL) {
        textView.setTypeface(textView.typeface, typeface)
    }

    @JvmOverloads
    @ModelProp
    fun background(@DrawableRes bgResource: Int = R.color.bgPrimary) {
        setBackgroundResource(bgResource)
    }

    @ModelProp
    fun textSize(@Px size: Float) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    @JvmOverloads
    @CallbackProp
    fun onButtonClickListener(listener: ((View) -> Unit)? = null) {
        button.setOnClickListener { listener?.invoke(this) }
    }

    @ModelProp
    fun buttonImageResource(@DrawableRes imgResource: Int) {
        button.setImageResource(imgResource)
    }

    @JvmOverloads
    @ModelProp
    fun buttonTint(@ColorInt tint: Int = Color.WHITE) {
        button.setColorFilter(tint)
    }

    @JvmOverloads
    @ModelProp
    fun buttonPadding(@Px size: Int = 0) {
        button.setPadding(size)
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