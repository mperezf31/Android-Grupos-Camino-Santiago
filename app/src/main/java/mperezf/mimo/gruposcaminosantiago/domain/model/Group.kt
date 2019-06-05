package mperezf.mimo.gruposcaminosantiago.domain.model

data class Group(
    val id: Int?,
    val photo: String?,
    val title: String?,
    val description: String?,
    val departureDate: Int?,
    val arrivalDate: Int?,
    val departurePlace: String?,
    val latitude: Double?,
    val longitude: Double?,
    val founder: User?,
    val members: List<User>?,
    val messages: List<Message>?
)