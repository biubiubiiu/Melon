package app.melon.home.nearby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.melon.R
import app.melon.base.uikit.TagView
import app.melon.data.entities.User
import coil.load
import coil.transform.CircleCropTransformation

class NearbyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val avatarView: ImageView = view.findViewById(R.id.nearby_user_avatar)
    private val usernameView: TextView = view.findViewById(R.id.nearby_username)
    private val userTagView: TagView = view.findViewById(R.id.nearby_user_tag)
    private val userDescriptionView: TextView = view.findViewById(R.id.nearby_user_description)
    private val schoolView: TextView = view.findViewById(R.id.nearby_user_school)
    private val distance: TextView = view.findViewById(R.id.nearby_user_distance)

    fun bind(user: User?) {
        if (user == null) {
            // TODO use shimmer layout and enable placeholder
        } else {
            avatarView.load(user.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = user.username
            userTagView.bind(user)
            userDescriptionView.text = user.description
            schoolView.text = user.school
            distance.text = "1km" // TODO import location module
        }
    }

    companion object {
        fun create(parent: ViewGroup): NearbyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_nearby_user, parent, false)
            return NearbyViewHolder(view)
        }
    }
}