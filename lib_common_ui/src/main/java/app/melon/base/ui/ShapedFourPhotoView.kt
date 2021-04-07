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
class ShapedFourPhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_ROW: Int = 2
        private const val MAX_COL: Int = 2
        private const val MAX_ITEM: Int = MAX_ROW * MAX_COL
    }

    var urls: List<String> = emptyList()
        @ModelProp set

    var itemPadding: Int = 0
        @ModelProp set

    var cornerRadius: Float = 0f
        @ModelProp set

    var onClickListener: ((List<String>, Int, ImageView) -> Unit)? = null
        @CallbackProp set

    var whRatio: Float = 1.74f
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
        initAreaMap()
        removeAllViews()
        for (i in 0 until displayItemCount) {
            addView(getView(urls, i))
        }
    }

    private val displayItemCount get() = min(urls.size, MAX_ITEM)
    private val row get() = MAX_ROW
    private val col get() = MAX_COL

    private var smallestItemWidth = 0
    private var smallestItemHeight = 0

    private fun getPosition(index: Int): Pair<Int, Int> {
        if (displayItemCount == 3) {
            val row = if (index == 2) 1 else 0
            val col = if (index == 0) 0 else 1
            return Pair(row, col)
        }
        return Pair(index / col, index % col)
    }

    private fun getSpan(index: Int): Pair<Int, Int> {
        return when (displayItemCount) {
            1 -> Pair(2, 2)
            2 -> Pair(2, 1)
            3 -> if (index == 0) Pair(2, 1) else Pair(1, 1)
            else -> Pair(1, 1)
        }
    }

    private fun initAreaMap() {
        for (i in 0 until displayItemCount) {
            val (start_row, start_col) = getPosition(i)
            val (span_row, span_col) = getSpan(i)
            for (r in start_row until start_row + span_row) {
                for (c in start_col until start_col + span_col) {
                    areaMap[r][c] = i
                }
            }
        }
    }

    private val areaMap: Array<IntArray> = Array(MAX_ROW) { IntArray(MAX_COL) { -1 } }

    private fun getView(urls: List<String>, position: Int): View {
        val url = urls[position]
        return ShapeableImageView(context).apply {
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setTopLeftCornerSize(if (areaMap[0][0] == position) cornerRadius else 0f)
                .setTopRightCornerSize(if (areaMap[0][MAX_COL - 1] == position) cornerRadius else 0f)
                .setBottomLeftCornerSize(if (areaMap[MAX_ROW - 1][0] == position) cornerRadius else 0f)
                .setBottomRightCornerSize(if (areaMap[MAX_ROW - 1][MAX_COL - 1] == position) cornerRadius else 0f)
                .build()
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener { onClickListener?.invoke(urls, position, this) }
            load(url) {
                allowHardware(false)
                placeholder(R.drawable.image_placeholder)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (displayItemCount > 0) {
            val layoutWidth = MeasureSpec.getSize(widthMeasureSpec)
            val photoAreaWidth = layoutWidth - paddingLeft - paddingRight
            val photoAreaHeight = (photoAreaWidth / whRatio).toInt()
            val layoutHeight = photoAreaHeight + paddingTop + paddingBottom
            smallestItemWidth = (photoAreaWidth - itemPadding * (MAX_COL - 1)) / MAX_COL
            smallestItemHeight = (photoAreaHeight - itemPadding * (MAX_ROW - 1)) / MAX_ROW
            for (i in 0 until childCount) {
                val (span_row, span_col) = getSpan(i)
                val itemWidth = smallestItemWidth + (span_col - 1) * (itemPadding + smallestItemWidth)
                val itemHeight = smallestItemHeight + (span_row - 1) * (itemPadding + smallestItemHeight)
                getChildAt(i).measure(
                    MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)
                )
            }
            setMeasuredDimension(layoutWidth, layoutHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val (actual_row, actual_col) = getPosition(i)
            val (span_row, span_col) = getSpan(i)
            val left = (smallestItemWidth + itemPadding) * actual_col + paddingLeft
            val right = left + (smallestItemWidth + itemPadding) * (span_col - 1) + smallestItemWidth
            val top = (smallestItemHeight + itemPadding) * actual_row + paddingTop
            val bottom = top + (smallestItemHeight + itemPadding) * (span_row - 1) + smallestItemHeight
            val childView = getChildAt(i)
            childView.layout(left, top, right, bottom)
        }
    }
}