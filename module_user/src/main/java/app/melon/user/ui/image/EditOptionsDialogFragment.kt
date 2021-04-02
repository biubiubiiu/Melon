package app.melon.user.ui.image

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.melon.user.R

class EditOptionsDialogFragment : DialogFragment() {

    private var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_options, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.from_album).setOnClickListener {
            listener?.onSelectOptionAlbum()
            dismiss()
        }
        view.findViewById<TextView>(R.id.from_camera).setOnClickListener {
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