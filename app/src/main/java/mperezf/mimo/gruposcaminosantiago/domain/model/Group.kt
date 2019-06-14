package mperezf.mimo.gruposcaminosantiago.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Group(
    val id: Int? = 0,
    val photo: String?,
    val title: String?,
    val description: String?,
    val departureDate: Long?,
    val arrivalDate: Long?,
    val departurePlace: String?,
    val latitude: Double?,
    val longitude: Double?,
    val founder: User? = null,
    val members: List<User>? = emptyList(),
    val messages: List<Message>? = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readParcelable(User::class.java.classLoader),
        parcel.createTypedArrayList(User),
        parcel.createTypedArrayList(Message)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(photo)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(departureDate)
        parcel.writeValue(arrivalDate)
        parcel.writeString(departurePlace)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeParcelable(founder, flags)
        parcel.writeTypedList(members)
        parcel.writeTypedList(messages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }
}