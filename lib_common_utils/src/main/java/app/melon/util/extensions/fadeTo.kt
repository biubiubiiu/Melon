package app.melon.util.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * Fade a view to visible or gone. This function is idempotent - it can be called over and over again with the same
 * value without affecting an in-progress animation.
 *
 * taken from: https://gist.github.com/gpeal/2784b455cfd22d7ba567fa9c24144656
 */
fun View.fadeTo(visible: Boolean, duration: Long = 500, startDelay: Long = 0, toAlpha: Float = 1f) {
    // Make this idempotent.
    val tagKey = "fadeTo".hashCode()
    if (visible == isVisible && animation == null && getTag(tagKey) == null) return
    if (getTag(tagKey) == visible) return

    setTag(tagKey, visible)
    setTag("fadeToAlpha".hashCode(), toAlpha)

    if (visible && alpha == 1f) alpha = 0f
    animate()
        .alpha(if (visible) toAlpha else 0f)
        .withStartAction {
            if (visible) isVisible = true
        }
        .withEndAction {
            setTag(tagKey, null)
            if (isAttachedToWindow && !visible) isVisible = false
        }
        .setInterpolator(FastOutSlowInInterpolator())
        .setDuration(duration)
        .setStartDelay(startDelay)
        .start()
}

/**
 * Cancels the animation started by [fadeTo] and jumps to the end of it.
 */
fun View.cancelFade() {
    val tagKey = "fadeTo".hashCode()
    val visible = getTag(tagKey)?.castOrNull<Boolean>() ?: return
    animate().cancel()
    isVisible = visible
    alpha = if (visible) getTag("fadeToAlpha".hashCode())?.castOrNull<Float>() ?: 1f else 0f
    setTag(tagKey, null)
}

/**
 * Cancels the fade for this view and any ancestors.
 */
fun View.cancelFadeRecursively() {
    cancelFade()
    castOrNull<ViewGroup>()?.children?.asSequence()?.forEach { it.cancelFade() }
}

private inline fun <reified T> Any.castOrNull(): T? {
    return this as? T
}