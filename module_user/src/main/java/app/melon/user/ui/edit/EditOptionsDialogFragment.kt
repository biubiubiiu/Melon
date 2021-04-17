package app.melon.user.ui.edit

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import app.melon.user.R
import app.melon.user.databinding.FragmentEditOptionsBinding
import app.melon.util.delegates.viewBinding


class EditOptionsDialogFragment : DialogFragment(R.layout.fragment_edit_options) {

    private val binding: FragmentEditOptionsBinding by viewBinding()

    private var listener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fromAlbum.setOnClickListener {
            listener?.onSelectOptionAlbum()
            dismiss()
        }
        binding.fromCamera.setOnClickListener {
            listener?.onSelectOptionCamera()
            dismiss()
        }
    }

    internal fun setListener(listener: Listener) {
        this.listener = listener
    }

    internal interface Listener {
        fun onSelectOptionCamera()
        fun onSelectOptionAlbum()
    }
}