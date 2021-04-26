package app.melon.composer.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import app.melon.base.ui.MelonButton
import app.melon.base.ui.extensions.makeInvisible
import app.melon.base.ui.extensions.makeVisible
import app.melon.composer.R
import app.melon.composer.databinding.ViewToolbarContentBinding

internal class ComposerToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewToolbarContentBinding.inflate(LayoutInflater.from(context), this)

    internal var onClose: (() -> Unit)? = null
    internal var onTrailingAreaClicked: (() -> Unit)? = null

    internal var showCloseEntry: Boolean = true
        set(value) {
            field = value
            if (value) {
                binding.close.makeVisible()
            } else {
                binding.close.makeInvisible()
            }
        }

    internal var labelText: String = ""
        set(value) {
            field = value
            binding.label.text = value
        }

    internal var titleText: String = ""
        set(value) {
            field = value
            binding.title.text = value
        }

    private var showTrailingButton: Boolean = false
        set(value) {
            field = value
            binding.trailingButton.isVisible = value
        }

    internal var trailingButtonText: String = ""
        set(value) {
            field = value
            binding.trailingButton.text = value
        }

    private var showTrailingText: Boolean = false
        set(value) {
            field = value
            binding.trailingText.isVisible = value
        }

    internal var trailingText: String = ""
        set(value) {
            field = value
            binding.trailingText.text = value
        }

    internal var enableButton: Boolean = true
        set(value) {
            field = value
            binding.trailingButton.isEnabled = value
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.ComposerToolbar) {
            showCloseEntry = getBoolean(R.styleable.ComposerToolbar_showCloseEntry, true)
            labelText = getString(R.styleable.ComposerToolbar_labelText) ?: ""
            titleText = getString(R.styleable.ComposerToolbar_titleText) ?: ""
            trailingButtonText = getString(R.styleable.ComposerToolbar_trailingButtonText) ?: ""
            trailingText = getString(R.styleable.ComposerToolbar_trailingText) ?: ""

            val trailingStyle = TrailingStyle.values()[getInt(R.styleable.ComposerToolbar_trailingStyle, 0)]
            showTrailingText = trailingStyle == TrailingStyle.TEXT
            showTrailingButton = trailingStyle == TrailingStyle.BUTTON

            val buttonStyle =
                MelonButton.Style.values()[getInt(R.styleable.ComposerToolbar_defaultButtonStyle, 0)]
            binding.trailingButton.update(buttonStyle)
        }
        binding.close.setOnClickListener { onClose?.invoke() }
        binding.trailingButton.setOnClickListener { onTrailingAreaClicked?.invoke() }
        binding.trailingText.setOnClickListener { onTrailingAreaClicked?.invoke() }
    }

    private enum class TrailingStyle {
        NONE, TEXT, BUTTON
    }
}