package app.melon.composer.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import app.melon.composer.R
import app.melon.util.extensions.dp
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getColorCompat


internal class CheckView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var checked = false
        set(value) {
            field = value
            invalidate()
        }

    private lateinit var mStrokePaint: Paint
    private lateinit var mShadowPaint: Paint
    private lateinit var mBackgroundPaint: Paint
    private lateinit var mCheckDrawable: Drawable

    private val mSize = 48.dpInt
    private val mContentSize = 16.dpInt

    private val mStrokeWidth = 3f.dp
    private val mStrokeRadius = 11.5f.dp
    private val mShadowWidth = 6f.dp
    private val mBackgroundRadius = 11f.dp

    // rect for drawing checked number or mark
    private val checkRect: Rect by lazy {
        val rectPadding = (mSize - mContentSize) / 2
        Rect(
            rectPadding, rectPadding,
            mSize - rectPadding, mSize - rectPadding
        )
    }

    init {
        initCheckDrawable()
        initStrokePaint()
        initShadowPaint()
        initBackgroundPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // fixed size 48dp x 48dp
        val sizeSpec = MeasureSpec.makeMeasureSpec(mSize, MeasureSpec.EXACTLY)
        super.onMeasure(sizeSpec, sizeSpec)
    }

    override fun setEnabled(enabled: Boolean) {
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw outer and inner shadow
        canvas.drawCircle(
            mSize / 2f, mSize / 2f,
            mStrokeRadius + mStrokeWidth / 2f + mShadowWidth, mShadowPaint
        )

        // draw white stroke
        canvas.drawCircle(
            mSize / 2f, mSize / 2f,
            mStrokeRadius, mStrokePaint
        )

        // draw content
        if (checked) {
            canvas.drawCircle(
                mSize / 2f, mSize / 2f,
                mBackgroundRadius, mBackgroundPaint
            )
            mCheckDrawable.bounds = checkRect
            mCheckDrawable.draw(canvas)
        }

        // enable hint
        alpha = if (isEnabled) 1.0f else 0.5f
    }

    private fun initCheckDrawable() {
        mCheckDrawable = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.ic_check_white_18dp,
            context.theme
        )!!
    }

    private fun initStrokePaint() {
        mStrokePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            strokeWidth = mStrokeWidth
            color = Color.WHITE
        }
    }

    private fun initShadowPaint() {
        mShadowPaint = Paint()
        mShadowPaint.isAntiAlias = true
        // all in dp
        val outerRadius = mStrokeRadius + mStrokeWidth / 2
        val innerRadius = outerRadius - mStrokeWidth
        val gradientRadius = outerRadius + mShadowWidth
        val stop0 = (innerRadius - mShadowWidth) / gradientRadius
        val stop1 = innerRadius / gradientRadius
        val stop2 = outerRadius / gradientRadius
        val stop3 = 1.0f
        mShadowPaint.shader = RadialGradient(
            mSize / 2f,
            mSize / 2f,
            gradientRadius, intArrayOf(
                Color.parseColor("#00000000"), Color.parseColor("#0D000000"),
                Color.parseColor("#0D000000"), Color.parseColor("#00000000")
            ), floatArrayOf(stop0, stop1, stop2, stop3),
            Shader.TileMode.CLAMP
        )
    }

    private fun initBackgroundPaint() {
        mBackgroundPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = context.getColorCompat(R.color.colorPrimary)
        }
    }
}