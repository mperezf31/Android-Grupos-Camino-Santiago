package mperezf.mimo.gruposcaminosantiago.domain.model

data class MessageData(
    val id: Int,
    val author: UserData,
    val content: String,
    val whenSent: Int
)
