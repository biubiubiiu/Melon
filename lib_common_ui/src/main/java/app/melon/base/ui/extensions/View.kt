package app.melon.base.ui.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewTreeObserver

/**
 * Taken from: https://github.com/stfalcon-studio/StfalconImageViewer
 */
val View?.localVisibleRect: Rect
    get() = Rect().also { this?.getLocalVisibleRect(it) }

val View?.globalVisibleRect: Rect
    get() = Rect().also { this?.getGlobalVisibleRect(it) }

val View?.hitRect: Rect
    get() = Rect().also { this?.getHitRect(it) }

val View?.isRectVisible: Boolean
    get() = this != null && globalVisibleRect != localVisibleRect

val View?.isVisible: Boolean
    get() = this != null && visibility == View.VISIBLE

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

inline fun <T : View> T.postApply(crossinline block: T.() -> Unit) {
    post { apply(block) }
}

inline fun <T : View> T.postDelayed(delayMillis: Long, crossinline block: T.() -> Unit) {
    postDelayed({ block() }, delayMillis)
}

fun View.applyMargin(
    start: Int? = null,
    top: Int? = null,
    end: Int? = null,
    bottom: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            marginStart = start ?: marginStart
            topMargin = top ?: topMargin
            marginEnd = end ?: marginEnd
            bottomMargin = bottom ?: bottomMargin
        }
    }
}

fun View.requestNewSize(
    width: Int, height: Int
) {
    layoutParams.width = width
    layoutParams.height = height
    layoutParams = layoutParams
}

fun View.makeViewMatchParent() {
    applyMargin(0, 0, 0, 0)
    requestNewSize(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
}

fun View.animateAlpha(from: Float?, to: Float?, duration: Long) {
    alpha = from ?: 0f
    clearAnimation()
    animate()
        .alpha(to ?: 0f)
        .setDuration(duration)
        .start()
}

fun View.switchVisibilityWithAnimation() {
    val isVisible = visibility == View.VISIBLE
    val from = if (isVisible) 1.0f else 0.0f
    val to = if (isVisible) 0.0f else 1.0f

    ObjectAnimator.ofFloat(this, "alpha", from, to).apply {
        duration = ViewConfiguration.getDoubleTapTimeout().toLong()

        if (isVisible) {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    makeGone()
                }
            })
        } else {
            makeVisible()
        }

        start()
    }
}

inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            this@waitForLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            f()
        }
    })
}

