package app.melon.data.entities

import androidx.annotation.StringDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id") override val id: String = "",
    @ColumnInfo(name = "username") val username: String? = null,
    @ColumnInfo(name = "custom_id") val customId: String? = null,
    @ColumnInfo(name = "gender") @Gender val gender: String? = null,
    @ColumnInfo(name = "age") val age: Int? = null,
    @ColumnInfo(name = "hometown") val hometown: String? = null,
    @ColumnInfo(name = "school") val school: String? = null,
    @ColumnInfo(name = "college") val college: String? = null,
    @ColumnInfo(name = "major") val major: String? = null,
    @ColumnInfo(name = "degree") val degree: String? = null,
    @ColumnInfo(name = "location") val location: Location? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "follower_count") val followerCount: Long? = null,
    @ColumnInfo(name = "following_count") val followingCount: Long? = null,
    @ColumnInfo(name = "photos_url") val photos: List<String> = emptyList(),
    @ColumnInfo(name = "avatar_url") val avatarUrl: String? = null,
    @ColumnInfo(name = "background_url") val backgroundUrl: String? = null
) : MelonEntity {
    val isMale get() = gender == MALE
    val isFemale get() = gender == FEMALE
    val isHybrid get() = gender == HYBRID
    val isTransgender get() = gender == TRANSGENDER
    val isGenderless get() = gender == GENDERLESS
}

const val MALE = "Male"
const val FEMALE = "Female"
const val HYBRID = "Hybrid"
const val TRANSGENDER = "Transgender"
const val GENDERLESS = "Genderless"

val validGenders = setOf(
    MALE,
    FEMALE,
    HYBRID,
    TRANSGENDER,
    GENDERLESS
)

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    MALE,
    FEMALE,
    HYBRID,
    TRANSGENDER,
    GENDERLESS
)
annotation class Gender

fun String.isValidGender(): Boolean = validGenders.contains(this)