package app.melon.util.timesync

import com.google.gson.annotations.SerializedName

data class TimeResponse(
    @SerializedName("time") val time: String
)