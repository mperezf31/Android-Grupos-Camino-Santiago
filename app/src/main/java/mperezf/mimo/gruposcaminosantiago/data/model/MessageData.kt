package mperezf.mimo.gruposcaminosantiago.data.model

data class MessageData(
    val id: Int,
    val author: UserData,
    val content: String,
    val whenSent: Int
)
