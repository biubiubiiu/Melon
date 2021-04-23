package app.melon.base.ui.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator


fun ViewPropertyAnimator.setAnimatorListener(
    onAnimationEnd: ((Animator?) -> Unit)? = null,
    onAnimationStart: ((Animator?) -> Unit)? = null
): ViewPropertyAnimator = this.setListener(
    object : AnimatorListenerAdapter() {

        override fun onAnimationEnd(animation: Animator?) {
            onAnimationEnd?.invoke(animation)
        }

        override fun onAnimationStart(animation: Animator?) {
            onAnimationStart?.invoke(animation)
        }
    })