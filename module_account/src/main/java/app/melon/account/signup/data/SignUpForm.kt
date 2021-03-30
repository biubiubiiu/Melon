package app.melon.account.signup.data

import android.os.Parcel
import android.os.Parcelable


data class SignUpForm(
    val username: String,
    val phone: String,
    val email: String,
    val birthDate: String,
    val password: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(birthDate)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SignUpForm> {
        override fun createFromParcel(parcel: Parcel): SignUpForm {
            return SignUpForm(parcel)
        }

        override fun newArray(size: Int): Array<SignUpForm?> {
            return arrayOfNulls(size)
        }
    }
}