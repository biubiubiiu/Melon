package app.melon.util.extensions

import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView


/**
 * Taken from https://stackoverflow.com/a/45727769
 */
fun TextView.makeLinks(
    links: Map<String, View.OnClickListener>,
    withUnderline: Boolean = false
) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor
                textPaint.isUnderlineText = withUnderline
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.value.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.key, startIndexOfLink + 1)
        if (startIndexOfLink == -1) {
            continue
        }
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.key.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}