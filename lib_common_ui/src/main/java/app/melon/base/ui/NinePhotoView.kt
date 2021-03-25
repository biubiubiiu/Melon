package app.melon.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.updateMargins
import coil.load
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.math.min

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NinePhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_ITEM: Int = 9
        private const val MAX_ROW: Int = 3
        private const val MAX_COL: Int = 3
    }

    var urls: List<String> = emptyList()
        @ModelProp set

    var itemPadding: Int = 0
        @ModelProp set

    var cornerRadius: Float = 0f
        @ModelProp set

    var onClickListener: ((List<String>, Int) -> Unit)? = null
        @CallbackProp set

    @ModelProp
    fun marginHorizontal(size: Int) {
        (layoutParams as MarginLayoutParams).updateMargins(
            left = size,
            right = size
        )
        requestLayout()
    }

    @JvmOverloads
    @ModelProp
    fun background(@DrawableRes bgResource: Int = R.color.bgPrimary) {
        setBackgroundResource(bgResource)
    }

    @AfterPropsSet
    fun loadImage() {
        removeAllViews()
        for (i in 0 until itemCount) {
            addView(getImageView(urls, i))
        }
    }

    private val itemCount get() = urls.size
    private val row get() = min(MAX_ROW, (itemCount + 2) / 3)
    private val col get() = if (itemCount == 4) 2 else min(3, itemCount)

    private var itemSize = 0

    private fun getPosition(index: Int): Pair<Int, Int> {
        return Pair(index / col, index % col)
    }

    private fun getImageView(urls: List<String>, position: Int): View {
        val url = urls[position]
        return ShapeableImageView(context).apply {
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(cornerRadius)
                .build()
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener { onClickListener?.invoke(urls, position) }
            load(url) {
                allowHardware(false)
                placeholder(R.drawable.image_placeholder)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val layoutWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        if (itemCount > 0) {
            itemSize = (layoutWidth - itemPadding * (MAX_COL - 1)) / MAX_COL
            measureChildren(
                MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
            )
            val layoutHeight = itemSize * row + itemPadding * (row - 1) + paddingTop + paddingBottom
            setMeasuredDimension(layoutWidth, layoutHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount: Int = min(MAX_ITEM, itemCount)
        for (i in 0 until childCount) {
            val (actual_row, actual_col) = getPosition(i)
            val left = (itemSize + itemPadding) * actual_col + paddingLeft
            val top = (itemSize + itemPadding) * actual_row + paddingTop
            val right = left + itemSize
            val bottom = top + itemSize
            val childrenView = getChildAt(i)
            childrenView.layout(left, top, right, bottom)
        }
    }
}