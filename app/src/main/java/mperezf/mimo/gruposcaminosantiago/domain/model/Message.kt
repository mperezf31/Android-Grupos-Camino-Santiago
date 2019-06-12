package mperezf.mimo.gruposcaminosantiago.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Message(
    val id: Int? = 0,
    val author: User? = null,
    val content: String? = "",
    val whenSent: Long? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(User::class.java.classLoader),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(author, flags)
        parcel.writeString(content)
        parcel.writeValue(whenSent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}

data class MessageGroup(

    var idGroup: Int,
    var message: Message

)
