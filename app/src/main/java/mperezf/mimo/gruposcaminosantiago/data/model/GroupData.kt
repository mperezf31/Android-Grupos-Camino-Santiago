package mperezf.mimo.gruposcaminosantiago.data.model

data class GroupData(
    val id: Int?,
    val photo: String?,
    val title: String?,
    val description: String?,
    val departureDate: Int?,
    val arrivalDate: Int?,
    val departurePlace: String?,
    val latitude: Double?,
    val longitude: Double?,
    val founder: UserData?,
    val members: List<UserData>?,
    val posts: List<MessageData>?
)