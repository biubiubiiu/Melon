package app.melon.event.data.remote

import com.google.gson.annotations.SerializedName

data class EventStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("organiser") val organiser: EventListUserStruct = EventListUserStruct(),
    @SerializedName("type") val type: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("start_time") val startTime: String = "",
    @SerializedName("end_time") val endTime: String = "",
    @SerializedName("location") val location: String = "",
    @SerializedName("cost") val cost: Float? = null,
    @SerializedName("demand") val demand: String? = null
)

data class EventListUserStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("avatarURL") val avatarUrl: String? = null
)