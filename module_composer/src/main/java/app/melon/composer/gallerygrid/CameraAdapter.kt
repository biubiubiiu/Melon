package app.melon.composer.gallerygrid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.melon.composer.R

internal class CameraAdapter(
    private val onClick: () -> Unit
) : RecyclerView.Adapter<CameraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_gallery_camera, parent, false)
        return CameraViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        // No-op
    }

    override fun getItemCount(): Int = 1

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_gallery_camera
    }
}

internal class CameraViewHolder(
    view: View,
    onClick: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener { onClick.invoke() }
    }
}