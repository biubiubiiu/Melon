package app.melon.data.entities

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class User(
    @PrimaryKey @SerializedName("uid") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("school") val school: String = "",
    @SerializedName("college") val college: String = "",
    @SerializedName("major") val major: String = "",
    @SerializedName("degree") val degree: String = "",
    @SerializedName("avatar") val avatarUrl: String = "",
    @SerializedName("location") val location: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("age") val age: Int = 0,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("hometown") val hometown: String = "",
    @SerializedName("background") val backgroundUrl: String = "",
    @SerializedName("followers") val followerCount: Long,
    @SerializedName("following") val followingCount: Long,
    @SerializedName("photos") val photos: List<String> = emptyList()
) {
    val isMale get() = gender == "Male"
    val isFemale get() = gender == "Female"
    val isHybrid get() = gender == "Hybrid"
    val isTransgender get() = gender == "Transgender"
    val isGenderless get() = gender == "Genderless"
}