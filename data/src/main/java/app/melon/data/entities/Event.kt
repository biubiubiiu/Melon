package app.melon.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Event(
    @SerializedName("id") val id: String = "",
    @SerializedName("organiser") val organiser: User? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("title") val title: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("start_time") val startTime: String = "",
    @SerializedName("end_time") val endTime: String = "",
    @SerializedName("location") val location: String,
    @SerializedName("cost") val cost: Float? = null,
    @SerializedName("demand") val demand: String? = null
) : Serializable