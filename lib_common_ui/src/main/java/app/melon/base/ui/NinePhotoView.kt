package app.melon.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.updatePadding
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
        private const val MAX_ROW: Int = 3
        private const val MAX_COL: Int = 3
        private const val MAX_ITEM: Int = MAX_ROW * MAX_COL
    }

    var urls: List<String> = emptyList()
        @ModelProp set

    var itemPadding: Int = 0
        @ModelProp set

    var cornerRadius: Float = 0f
        @ModelProp set

    var whRatio: Float = 1f
        @ModelProp set

    var onClickListener: ((List<String>, Int) -> Unit)? = null
        @CallbackProp set

    @DrawableRes var placeholder: Int? = null
        @ModelProp set

    @JvmOverloads
    @ModelProp
    fun paddingHorizontal(size: Int = 0) {
        updatePadding(
            left = size, right = size, top = 0, bottom = 0
        )
    }

    @JvmOverloads
    @ModelProp
    fun background(@DrawableRes bgResource: Int = R.color.bgPrimary) {
        setBackgroundResource(bgResource)
    }

    @AfterPropsSet
    fun loadImage() {
        removeAllViews()
        for (i in 0 until displayItemCount) {
            addView(getImageView(urls, i))
        }
    }

    private val enablePlaceHolder get() = placeholder != null

    private val displayItemCount get() = if (enablePlaceHolder) MAX_ITEM else min(urls.size, MAX_ITEM)
    private val row get() = if (enablePlaceHolder) MAX_ROW else min(MAX_ROW, (displayItemCount + 2) / 3)
    private val col
        get() = when {
            enablePlaceHolder -> MAX_COL
            displayItemCount == 4 -> 2
            else -> min(3, displayItemCount)
        }

    private var itemWidth = 0
    private var itemHeight = 0

    private fun getPosition(index: Int): Pair<Int, Int> {
        return Pair(index / col, index % col)
    }

    private fun getImageView(urls: List<String>, position: Int): View {
        val url = urls.getOrNull(position)
        return ShapeableImageView(context).apply {
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCornerSizes(cornerRadius)
                .build()
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener { onClickListener?.invoke(urls, position) }
            if (url != null) {
                load(url) {
                    allowHardware(false)
                    placeholder(R.drawable.image_placeholder)
                }
            } else {
                load(placeholder!!)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (displayItemCount > 0) {
            val layoutWidth = MeasureSpec.getSize(widthMeasureSpec)
            val photoAreaWidth = layoutWidth - paddingLeft - paddingRight
            itemWidth = (photoAreaWidth - itemPadding * (MAX_COL - 1)) / MAX_COL
            itemHeight = (itemWidth / whRatio).toInt()
            measureChildren(
                MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)
            )
            val layoutHeight = itemHeight * row + itemPadding * (row - 1) + paddingTop + paddingBottom
            setMeasuredDimension(layoutWidth, layoutHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val (actual_row, actual_col) = getPosition(i)
            val left = (itemWidth + itemPadding) * actual_col + paddingLeft
            val top = (itemHeight + itemPadding) * actual_row + paddingTop
            val right = left + itemWidth
            val bottom = top + itemHeight
            val childView = getChildAt(i)
            childView.layout(left, top, right, bottom)
        }
    }
}